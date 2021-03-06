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

package ru.art.reactive.service.interception;

import reactor.core.publisher.*;
import ru.art.reactive.service.model.ReactiveService.*;
import ru.art.reactive.service.specification.*;
import ru.art.service.*;
import ru.art.service.model.*;
import ru.art.service.model.ServiceResponse.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.*;
import static ru.art.service.ServiceModule.*;
import static ru.art.service.model.ServiceInterceptionResult.*;

@SuppressWarnings("Duplicates")
public class ReactiveServiceLoggingInterception extends ServiceLoggingInterception {
    @Override
    public ServiceInterceptionResult intercept(ServiceRequest<?> request) {
        String serviceId = request.getServiceMethodCommand().getServiceId();
        String methodId = request.getServiceMethodCommand().getMethodId();
        Specification serviceSpecification = serviceModuleState().getServiceRegistry().getService(serviceId);
        if (!serviceSpecification.getServiceTypes().contains(REACTIVE_SERVICE_TYPE)) {
            return super.intercept(request);
        }

        ReactiveServiceSpecification reactiveServiceSpecification = (ReactiveServiceSpecification) serviceSpecification;
        ReactiveMethod reactiveMethod;
        if (isNull(reactiveMethod = reactiveServiceSpecification.getReactiveService().getMethods().get(methodId))) {
            return super.intercept(request);
        }

        ReactiveMethodProcessingMode requestProcessingMode = reactiveMethod.requestProcessingMode();
        switch (requestProcessingMode) {
            case STRAIGHT:
                return super.intercept(request);
            case REACTIVE:
                return interceptReactive(request);
        }
        return nextInterceptor(request);
    }

    @Override
    public ServiceInterceptionResult intercept(ServiceRequest<?> request, ServiceResponse<?> response) {
        String serviceId = request.getServiceMethodCommand().getServiceId();
        String methodId = request.getServiceMethodCommand().getMethodId();
        Specification serviceSpecification = serviceModuleState().getServiceRegistry().getService(serviceId);
        if (!serviceSpecification.getServiceTypes().contains(REACTIVE_SERVICE_TYPE)) {
            return super.intercept(request, response);
        }

        ReactiveServiceSpecification reactiveServiceSpecification = (ReactiveServiceSpecification) serviceSpecification;
        ReactiveMethod reactiveMethod;
        if (isNull(reactiveMethod = reactiveServiceSpecification.getReactiveService().getMethods().get(methodId))) {
            return super.intercept(request, response);
        }

        ReactiveMethodProcessingMode requestProcessingMode = reactiveMethod.responseProcessingMode();
        switch (requestProcessingMode) {
            case STRAIGHT:
                return super.intercept(request, response);
            case REACTIVE:
                return interceptReactive(request, response);
        }
        return nextInterceptor(request, response);
    }

    private ServiceInterceptionResult interceptReactive(ServiceRequest<?> request) {
        if (isNull(request.getRequestData())) {
            return super.intercept(request);
        }
        Flux<?> requestStream = from(cast(request.getRequestData()))
                .doOnNext(payload -> super.intercept(new ServiceRequest<>(request.getServiceMethodCommand(), request.getValidationPolicy(), payload)));
        return nextInterceptor(new ServiceRequest<>(request.getServiceMethodCommand(), request.getValidationPolicy(), requestStream));
    }

    private ServiceInterceptionResult interceptReactive(ServiceRequest<?> request, ServiceResponse<?> response) {
        ServiceResponseBuilder<Flux<?>> responseBuilder = ServiceResponse.<Flux<?>>builder()
                .command(response.getCommand())
                .serviceException(response.getServiceException());
        if (nonNull(response.getResponseData())) {
            responseBuilder.responseData(from(cast(response.getResponseData()))
                    .doOnNext(payload -> super.intercept(request, ServiceResponse.builder()
                            .responseData(payload)
                            .serviceException(response.getServiceException())
                            .command(request.getServiceMethodCommand())
                            .build())));
        }
        return nextInterceptor(request, responseBuilder.build());
    }
}
