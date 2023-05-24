/*
 * Copyright 2021 DataCanvas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dingodb.client.operation.impl;

import io.dingodb.client.OperationContext;
import io.dingodb.client.common.Record;
import io.dingodb.client.common.RouteTable;
import io.dingodb.sdk.common.KeyValue;
import io.dingodb.sdk.common.codec.KeyValueCodec;
import io.dingodb.sdk.common.table.Table;
import io.dingodb.sdk.common.utils.Any;
import io.dingodb.sdk.common.utils.ByteArrayUtils;
import io.dingodb.sdk.common.utils.LinkedIterator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;

import static io.dingodb.client.operation.RangeUtils.getSubTasks;
import static io.dingodb.client.operation.RangeUtils.isInvalidRange;
import static io.dingodb.sdk.common.utils.Any.wrap;

public class ScanOperation implements Operation {

    private static final ScanOperation INSTANCE = new ScanOperation();

    private ScanOperation() {
    }

    public static ScanOperation getInstance() {
        return INSTANCE;
    }

    @Override
    public Fork fork(Any parameters, Table table, RouteTable routeTable) {
        try {
            KeyValueCodec codec = routeTable.codec;
            OpKeyRange keyRange = parameters.getValue();
            Object[] startKey = mapKeyPrefix(table, keyRange.start);
            Object[] endKey = mapKeyPrefix(table, keyRange.end);
            OpRange range = new OpRange(
                codec.encodeKeyPrefix(startKey, keyRange.start.userKey.size()),
                codec.encodeKeyPrefix(endKey, keyRange.end.userKey.size()),
                keyRange.withStart,
                keyRange.withEnd
            );
            if (isInvalidRange(startKey, endKey, range)) {
                return new Fork(new Iterator[0], Collections.emptyNavigableSet(), true);
            }
            NavigableSet<Task> subTasks = getSubTasks(routeTable, range);
            return new Fork(new Iterator[subTasks.size()], subTasks, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Fork fork(OperationContext context, RouteTable routeTable) {
        OpRange range = context.parameters();
        NavigableSet<Task> subTasks = getSubTasks(routeTable, range);
        return new Fork(context.result(), subTasks, true);
    }

    @Override
    public void exec(OperationContext context) {
        OpRange scan = context.parameters();

        Iterator<KeyValue> scanResult = context.getStoreService()
            .scan(context.getTableId(), context.getRegionId(), scan.range, scan.withStart, scan.withEnd);

        context.<Iterator<Record>[]>result()[context.getSeq()] = new RecordIterator(
            context.getTable().getColumns(), context.getCodec(), scanResult
        );
    }

    @Override
    public <R> R reduce(Fork fork) {
        LinkedIterator<Record> result = new LinkedIterator<>();
        Arrays.stream(fork.<Iterator<Record>[]>result()).forEach(result::append);
        return (R) result;
    }

}
