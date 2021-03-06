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

package ru.art.rsocket.function;

import ru.art.entity.*;
import ru.art.entity.interceptor.*;
import ru.art.entity.mapper.*;
import ru.art.reactive.service.constants.ReactiveServiceModuleConstants.*;
import ru.art.rsocket.constants.RsocketModuleConstants.*;
import ru.art.rsocket.service.RsocketService.*;
import ru.art.service.constants.*;
import ru.art.service.registry.*;
import static java.util.Objects.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.reactive.service.model.ReactiveService.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.service.ServiceModule.*;
import java.util.function.*;

public class RsocketServiceFunction {
    private RsocketMethod rsocketMethod = RsocketMethod.rsocketMethod();
    private ReactiveMethod reactiveMethod = ReactiveMethod.reactiveMethod();
    private final String functionId;

    private RsocketServiceFunction(String functionId) {
        this.functionId = functionId;
    }


    public RsocketServiceFunction overrideResponseDataFormat(RsocketDataFormat dataFormat) {
        rsocketMethod.overrideResponseDataFormat(dataFormat);
        return this;
    }

    public <ResponseType> RsocketServiceFunction responseMapper(ValueFromModelMapper<ResponseType, ? extends Value> responseMapper) {
        rsocketMethod.responseMapper(responseMapper);
        return this;
    }

    public <RequestMapper> RsocketServiceFunction requestMapper(ValueToModelMapper<RequestMapper, ? extends Value> requestMapper) {
        rsocketMethod.requestMapper(requestMapper);
        return this;
    }

    public RsocketServiceFunction addRequestValueInterceptor(ValueInterceptor<Entity, Entity> interceptor) {
        rsocketMethod.requestValueInterceptors().add(interceptor);
        return this;
    }

    public RsocketServiceFunction addResponseValueInterceptor(ValueInterceptor<Entity, Entity> interceptor) {
        rsocketMethod.responseValueInterceptors().add(interceptor);
        return this;
    }

    public RsocketServiceFunction requestProcessingMode(ReactiveMethodProcessingMode methodProcessingMode) {
        reactiveMethod.requestProcessingMode(methodProcessingMode);
        return this;
    }

    public RsocketServiceFunction responseProcessingMode(ReactiveMethodProcessingMode methodProcessingMode) {
        reactiveMethod.responseProcessingMode(methodProcessingMode);
        return this;
    }

    public RsocketServiceFunction validationPolicy(RequestValidationPolicy policy) {
        rsocketMethod.validationPolicy(policy);
        return this;
    }

    public <RequestType, ResponseType> void handle(Function<RequestType, ResponseType> function) {
        ServiceRegistry serviceRegistry = serviceModuleState().getServiceRegistry();
        RsocketFunctionalServiceSpecification specification = cast(serviceRegistry.getServices().get(RSOCKET_FUNCTION_SERVICE));
        if (isNull(specification)) {
            specification = new RsocketFunctionalServiceSpecification();
            specification.addFunction(functionId, rsocketMethod, reactiveMethod, function);
            serviceRegistry.registerService(specification);
            return;
        }
        specification.addFunction(functionId, rsocketMethod, reactiveMethod, function);
    }

    public <RequestType> void consume(Consumer<RequestType> consumer) {
        handle(request -> {
            consumer.accept(cast(request));
            return null;
        });
    }

    public <ResponseType> void produce(Supplier<ResponseType> producer) {
        handle(request -> producer.get());
    }

    public static RsocketServiceFunction rsocket(String functionId) {
        return new RsocketServiceFunction(functionId);
    }
}