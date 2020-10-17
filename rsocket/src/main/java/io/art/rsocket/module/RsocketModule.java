/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.rsocket.module;

import io.art.core.module.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.launcher.*;
import io.art.rsocket.state.*;
import lombok.*;
import static io.art.core.context.Context.*;
import static io.art.rsocket.configuration.RsocketModuleConfiguration.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;

@Getter
public class RsocketModule implements StatefulModule<RsocketModuleConfiguration, Configurator, RsocketModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatefulModuleProxy<RsocketModuleConfiguration, RsocketModuleState> rsocketModule = context().getStatefulModule(RsocketModule.class.getSimpleName());
    private final String id = RsocketModule.class.getSimpleName();
    private final RsocketModuleConfiguration configuration = new RsocketModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    private final RsocketModuleState state = new RsocketModuleState();
    private final RsocketManager manager = new RsocketManager(configuration, state);

    public static StatefulModuleProxy<RsocketModuleConfiguration, RsocketModuleState> rsocketModule() {
        return getRsocketModule();
    }

    @Override
    public void afterLoad() {
        if (nonNull(configuration.getCommunicatorConfiguration())) {
            manager.startConnectors();
        }
        if (nonNull(configuration.getServerConfiguration())) {
            manager.startSever();
        }
    }

    @Override
    public void beforeUnload() {
        manager.stopSever();
        manager.stopConnectors();
    }
}
