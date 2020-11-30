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

package io.art.model.customizer;

import io.art.model.configurator.*;
import lombok.*;
import lombok.experimental.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@Getter
@Setter
@Accessors(fluent = true)
public class ConfiguratorCustomizer {
    private UnaryOperator<LoggingConfiguratorModel> logging = identity();
    private UnaryOperator<ServerConfiguratorModel> server = identity();
    private UnaryOperator<ValueConfiguratorModel> value = identity();
    private UnaryOperator<RsocketConfiguratorModel> rsocket = identity();

    public ConfiguratorModel apply() {
        return ConfiguratorModel.builder()
                .logging(logging.apply(new LoggingConfiguratorModel()))
                .server(server.apply(new ServerConfiguratorModel()))
                .value(value.apply(new ValueConfiguratorModel()))
                .rsocket(rsocket.apply(new RsocketConfiguratorModel()))
                .build();
    }
}
