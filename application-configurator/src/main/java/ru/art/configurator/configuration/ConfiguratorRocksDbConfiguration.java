/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.configurator.configuration;

import lombok.Getter;
import ru.art.rocks.db.configuration.RocksDbModuleConfiguration.RocksDbModuleDefaultConfiguration;
import static ru.art.config.ConfigProvider.config;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.CONFIGURATOR_ROCKS_DB_PATH;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.CONFIGURATOR_SECTION_ID;

@Getter
public class ConfiguratorRocksDbConfiguration extends RocksDbModuleDefaultConfiguration {
    private boolean enableTracing = false;
    private String path = config(CONFIGURATOR_SECTION_ID).asYamlConfig().getString(CONFIGURATOR_ROCKS_DB_PATH);
}
