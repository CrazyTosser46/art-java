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

package io.art.launcher;

import com.google.common.collect.*;
import io.art.communicator.module.*;
import io.art.configurator.module.*;
import io.art.core.caster.*;
import io.art.core.configuration.ContextConfiguration.*;
import io.art.core.constants.*;
import io.art.core.context.*;
import io.art.core.lazy.*;
import io.art.core.module.Module;
import io.art.core.source.*;
import io.art.entity.factory.*;
import io.art.entity.immutable.*;
import io.art.json.module.*;
import io.art.logging.*;
import io.art.rsocket.module.*;
import io.art.server.decorator.*;
import io.art.server.implementation.*;
import io.art.server.model.*;
import io.art.server.module.*;
import io.art.server.specification.*;
import io.art.xml.module.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import static io.art.core.constants.MethodDecoratorScope.INPUT;
import static io.art.core.constants.MethodDecoratorScope.OUTPUT;
import static io.art.core.constants.MethodProcessingMode.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ThreadExtensions.block;
import static io.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static io.art.core.lazy.LazyValue.*;
import static io.art.entity.factory.ArrayFactory.*;
import static io.art.entity.factory.PrimitivesFactory.stringPrimitive;
import static io.art.entity.immutable.Entity.entityBuilder;
import static io.art.logging.LoggingModule.*;
import static io.art.server.implementation.ServiceMethodImplementation.producer;
import static io.art.server.model.ServiceMethodIdentifier.*;
import static io.art.server.module.ServerModule.specifications;
import java.time.*;
import java.time.format.*;
import java.util.concurrent.atomic.*;

@UtilityClass
public class ModuleLauncher {
    private final static AtomicBoolean launched = new AtomicBoolean(false);

    public static void launch(/*ModuleModel model*/) {
        if (launched.compareAndSet(false, true)) {
            ConfiguratorModule configurator = new ConfiguratorModule();
            ImmutableList<ConfigurationSource> sources = configurator
                    .loadConfigurations()
                    .configuration()
                    .orderedSources();
            //ConfiguratorModel configuratorModel = model.getConfiguratorModel();
            ImmutableList.Builder<Module> modules = ImmutableList.builder();
            ServerModule server = server(sources);
            modules.add(
                    configurator,
                    logging(sources/*, configuratorModel*/),
                    json(sources),
                    xml(sources),
                    server,
                    communicator(sources),
                    rsocket(sources)
            );
            LazyValue<Logger> logger = lazy(() -> logger(Context.class));
            initialize(new DefaultContextConfiguration(), modules.build(), message -> logger.get().info(message));
            ServiceMethodSpecification specification = ServiceMethodSpecification.builder()
                    .inputMapper(Caster::cast)
                    .outputMapper(Caster::cast)
                    .inputMode(BLOCKING)
                    .outputMode(BLOCKING)
                    .serviceId("test")
                    .methodId("test")
                    .implementation(producer(() -> {
                        System.out.println(RsocketModule.rsocketModule().state().localState());
                        return entityBuilder().lazyPut("test", () -> array(dynamicArrayOf(stringPrimitive("test")))).build();
                    }, "test", "test"))
                    .inputDecorator(new ServiceLoggingDecorator(serviceMethod("test", "test"), INPUT, () -> true))
                    .outputDecorator(new ServiceLoggingDecorator(serviceMethod("test", "test"), OUTPUT, () -> true))
                    .build();
            server.getState().getSpecifications().register(ServiceSpecification.builder()
                    .serviceId("test")
                    .method("test", specification)
                    .build()
            );
            block();
        }
    }

    private LoggingModule logging(ImmutableList<ConfigurationSource> sources/*, ConfiguratorModel configuratorModel*/) {
        LoggingModule logging = new LoggingModule();
        logging.configure(configurator -> configurator.from(sources));
//        ofNullable(configuratorModel.getLoggingConfigurator())
//                .ifPresent(model -> logging.configure(configurator -> configurator.from(model.getConfiguration())));
        return logging;
    }

    private JsonModule json(ImmutableList<ConfigurationSource> sources) {
        JsonModule json = new JsonModule();
        json.configure(configurator -> configurator.from(sources));
        return json;
    }

    private XmlModule xml(ImmutableList<ConfigurationSource> sources) {
        XmlModule xml = new XmlModule();
        xml.configure(configurator -> configurator.from(sources));
        return xml;
    }

    private ServerModule server(ImmutableList<ConfigurationSource> sources) {
        ServerModule server = new ServerModule();
        server.configure(configurator -> configurator.from(sources));
        return server;
    }

    private CommunicatorModule communicator(ImmutableList<ConfigurationSource> sources) {
        CommunicatorModule communicator = new CommunicatorModule();
        communicator.configure(configurator -> configurator.from(sources));
        return communicator;
    }

    private RsocketModule rsocket(ImmutableList<ConfigurationSource> sources) {
        RsocketModule rsocket = new RsocketModule();
        rsocket.configure(configurator -> configurator.from(sources));
        return rsocket;
    }

    public static void main(String[] args) {
        launch();
    }
}
