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

package io.dingodb.client;

import io.dingodb.client.common.KeyValueCodec;
import io.dingodb.sdk.common.DingoCommonId;
import io.dingodb.sdk.common.table.Table;
import io.dingodb.sdk.common.utils.Any;
import io.dingodb.sdk.service.store.StoreServiceClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class OperationContext {

    private final DingoCommonId tableId;
    private final DingoCommonId regionId;
    private final Table table;
    private final KeyValueCodec codec;

    private final StoreServiceClient storeService;

    private final int seq;
    private final Any parameters;
    private final Any result;

    public <P> P parameters() {
        return parameters.getValue();
    }

    public <R> R result() {
        return result.getValue();
    }

}
