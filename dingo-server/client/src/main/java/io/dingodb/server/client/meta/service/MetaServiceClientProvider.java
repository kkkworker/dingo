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

package io.dingodb.server.client.meta.service;

import com.google.auto.service.AutoService;
import io.dingodb.meta.MetaService;
import io.dingodb.meta.MetaServiceProvider;
import io.dingodb.server.client.connector.impl.CoordinatorConnector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoService(MetaServiceProvider.class)
public class MetaServiceClientProvider implements MetaServiceProvider {

    private static final MetaServiceClient META_SERVICE_CLIENT = new MetaServiceClient();

    @Override
    public MetaService root() {
        return META_SERVICE_CLIENT;
    }

    public MetaService root(CoordinatorConnector connector) {
        return new MetaServiceClient(connector);
    }
}
