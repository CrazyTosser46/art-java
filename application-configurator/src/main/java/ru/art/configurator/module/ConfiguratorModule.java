/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.configurator.module;

import lombok.Getter;
import ru.art.config.module.ConfigModule;
import ru.art.configurator.configuration.*;
import ru.art.configurator.specification.ConfiguratorServiceSpecification;
import ru.art.configurator.specification.UserServiceSpecification;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.grpc.client.module.GrpcClientModule;
import ru.art.grpc.server.module.GrpcServerModule;
import ru.art.http.client.module.HttpClientModule;
import ru.art.http.server.module.HttpServerModule;
import ru.art.http.server.specification.HttpWebUiServiceSpecification;
import ru.art.json.module.JsonModule;
import ru.art.logging.LoggingModule;
import ru.art.metrics.http.specification.MetricServiceSpecification;
import ru.art.metrics.module.MetricsModule;
import ru.art.rocks.db.module.RocksDbModule;
import ru.art.service.ServiceModule;
import static java.util.UUID.randomUUID;
import static ru.art.config.ConfigProvider.config;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.CONFIGURATOR_MODULE_ID;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.*;
import static ru.art.configurator.service.UserService.register;
import static ru.art.core.configuration.ContextInitialConfiguration.ApplicationContextConfiguration;
import static ru.art.core.context.Context.*;
import static ru.art.core.extension.ExceptionExtensions.ifExceptionOrEmpty;
import static ru.art.grpc.server.GrpcServer.grpcServer;
import static ru.art.http.server.HttpServer.httpServerInSeparatedThread;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.HttpPath.IMAGE_PATH;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;
import static ru.art.service.ServiceModule.serviceModule;

@Getter
public class ConfiguratorModule implements Module<ConfiguratorModuleConfiguration, ModuleState> {
    @Getter(lazy = true)
    private static final ConfiguratorModuleConfiguration configuratorModule = context().getModule(CONFIGURATOR_MODULE_ID, ConfiguratorModule::new);
    private final String id = CONFIGURATOR_MODULE_ID;
    private final ConfiguratorModuleConfiguration defaultConfiguration = new ConfiguratorModuleConfiguration();

    public static void startConfigurator() {
        ApplicationContextConfiguration configuration = new ApplicationContextConfiguration(CONFIGURATOR_MODULE_ID);
        initContext(configuration)
                .loadModule(new ConfigModule())
                .loadModule(new JsonModule())
                .loadModule(new LoggingModule())
                .loadModule(new ServiceModule())
                .loadModule(new RocksDbModule(), constructInsideDefaultContext(configuration, ConfiguratorRocksDbConfiguration::new))
                .loadModule(new HttpServerModule(), constructInsideDefaultContext(configuration, ConfiguratorHttpServerConfiguration::new))
                .loadModule(new GrpcServerModule(), constructInsideDefaultContext(configuration, ConfiguratorGrpcServerConfiguration::new))
                .loadModule(new MetricsModule(), constructInsideDefaultContext(configuration, ConfiguratorMetricsConfiguration::new))
                .loadModule(new GrpcClientModule())
                .loadModule(new HttpClientModule(), constructInsideDefaultContext(configuration, ConfiguratorHttpClientConfiguration::new))
                .loadModule(new ConfiguratorModule());
        String httpPath = httpServerModule().getPath();
        serviceModule()
                .getServiceRegistry()
                .registerService(new ConfiguratorServiceSpecification())
                .registerService(new HttpWebUiServiceSpecification(httpPath, httpPath + IMAGE_PATH))
                .registerService(new UserServiceSpecification())
                .registerService(new MetricServiceSpecification(httpPath));
        httpServerInSeparatedThread();
        grpcServer().await();
    }

    public static ConfiguratorModuleConfiguration configuratorModule() {
        return getConfiguratorModule();
    }

    public static void main(String[] args) {
        startConfigurator();
    }

    @Override
    public void onLoad() {
        String userName = ifExceptionOrEmpty(() -> config(CONFIGURATOR_SECTION_ID).getString(CONFIGURATOR_USERNAME), randomUUID().toString());
        String password = ifExceptionOrEmpty(() -> config(CONFIGURATOR_SECTION_ID).getString(CONFIGURATOR_PASSWORD), randomUUID().toString());
        register(userName, password);
    }
}
