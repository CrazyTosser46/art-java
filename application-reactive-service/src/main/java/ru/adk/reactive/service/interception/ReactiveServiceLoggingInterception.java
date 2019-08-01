package ru.adk.reactive.service.interception;

import reactor.core.publisher.Flux;
import ru.adk.reactive.service.model.ReactiveService.ReactiveMethod;
import ru.adk.reactive.service.specification.ReactiveServiceSpecification;
import ru.adk.service.ServiceLoggingInterception;
import ru.adk.service.Specification;
import ru.adk.service.model.ServiceInterceptionResult;
import ru.adk.service.model.ServiceRequest;
import ru.adk.service.model.ServiceResponse;
import ru.adk.service.model.ServiceResponse.ServiceResponseBuilder;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static reactor.core.publisher.Flux.from;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.REACTIVE_SERVICE_TYPE;
import static ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode;
import static ru.adk.service.ServiceModule.serviceModule;
import static ru.adk.service.model.ServiceInterceptionResult.nextInterceptor;

@SuppressWarnings("Duplicates")
public class ReactiveServiceLoggingInterception extends ServiceLoggingInterception {
    @Override
    public ServiceInterceptionResult intercept(ServiceRequest<?> request) {
        String serviceId = request.getServiceMethodCommand().getServiceId();
        String methodId = request.getServiceMethodCommand().getMethodId();
        Specification serviceSpecification = serviceModule().getServiceRegistry().getService(serviceId);
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
        Specification serviceSpecification = serviceModule().getServiceRegistry().getService(serviceId);
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
        Flux<Object> requestStream = from(cast(request.getRequestData()))
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
