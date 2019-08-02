package ru.art.grpc.client.communicator;

import io.grpc.ClientInterceptor;
import ru.art.core.validator.BuilderValidator;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.grpc.client.handler.GrpcCommunicationCompletionHandler;
import ru.art.grpc.client.handler.GrpcCommunicationExceptionHandler;
import ru.art.grpc.client.model.GrpcCommunicationTargetConfiguration;
import ru.art.service.model.ServiceResponse;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.StringConstants.COLON;
import java.util.concurrent.Executor;

public class GrpcCommunicatorImplementation implements GrpcCommunicator, GrpcCommunicator.GrpcAsynchronousCommunicator {
    private final GrpcCommunicationConfiguration configuration = new GrpcCommunicationConfiguration();
    private final BuilderValidator validator = new BuilderValidator(GrpcCommunicator.class.getName());

    GrpcCommunicatorImplementation(String host, int port, String path) {
        configuration.setUrl(validator.notEmptyField(host, "host") + COLON + validator.notNullField(port, "port"));
        configuration.setPath(validator.notEmptyField(path, "path"));
    }

    GrpcCommunicatorImplementation(String url, String path) {
        configuration.setUrl(validator.notEmptyField(url, "url"));
        configuration.setPath(validator.notEmptyField(path, "path"));
    }

    GrpcCommunicatorImplementation(String url) {
        configuration.setUrl(validator.notEmptyField(url, "url"));
    }

    GrpcCommunicatorImplementation(GrpcCommunicationTargetConfiguration targetConfiguration) {
        configuration.setPath(validator.notEmptyField(targetConfiguration.path(), "path"));
        deadlineTimeout(targetConfiguration.timeout());
        serviceId(targetConfiguration.serviceId());
        if (targetConfiguration.secured()) {
            secured();
        }
        if (isNotEmpty(targetConfiguration.url())) {
            configuration.setUrl(targetConfiguration.url());
            return;
        }
        configuration.setUrl(validator.notEmptyField(targetConfiguration.host(), "host") + COLON + validator.notNullField(targetConfiguration.port(), "port"));
    }

    @Override
    public GrpcCommunicator serviceId(String id) {
        configuration.setServiceId(validator.notEmptyField(id, "serviceId"));
        return this;
    }

    @Override
    public GrpcCommunicator methodId(String id) {
        configuration.setMethodId(validator.notEmptyField(id, "methodId"));
        return this;
    }

    @Override
    public GrpcCommunicator requestMapper(ValueFromModelMapper mapper) {
        configuration.setRequestMapper(validator.notNullField(cast(mapper), "requestMapper"));
        return this;
    }

    @Override
    public GrpcCommunicator responseMapper(ValueToModelMapper mapper) {
        configuration.setResponseMapper(validator.notNullField(cast(mapper), "responseMapper"));
        return this;
    }

    @Override
    public GrpcCommunicator deadlineTimeout(long timeout) {
        configuration.setDeadlineTimeout(timeout);
        return this;
    }

    @Override
    public GrpcCommunicator addInterceptor(ClientInterceptor interceptor) {
        configuration.getInterceptors().add(validator.notNullField(interceptor, "interceptor"));
        return this;
    }

    @Override
    public GrpcCommunicator executor(Executor executor) {
        configuration.setOverrideExecutor(validator.notNullField(executor, "executor"));
        return this;
    }

    @Override
    public GrpcCommunicator secured() {
        configuration.setUseSecuredTransport(true);
        return this;
    }

    @Override
    public GrpcAsynchronousCommunicator asynchronous() {
        return this;
    }

    @Override
    public <ResponseType> ServiceResponse<ResponseType> execute() {
        validator.validate();
        configuration.validateRequiredFields();
        return GrpcCommunicationExecutor.execute(configuration);
    }

    @Override
    public <RequestType, ResponseType> ServiceResponse<ResponseType> execute(RequestType request) {
        configuration.setRequest(validator.notNullField(request, "request"));
        validator.validate();
        configuration.validateRequiredFields();
        return GrpcCommunicationExecutor.execute(configuration);
    }

    @Override
    public <RequestType, ResponseType> GrpcAsynchronousCommunicator completionHandler(GrpcCommunicationCompletionHandler<RequestType, ResponseType> completionHandler) {
        configuration.setCompletionHandler(validator.notNullField(completionHandler, "completionHandler"));
        return this;
    }

    @Override
    public <RequestType> GrpcAsynchronousCommunicator exceptionHandler(GrpcCommunicationExceptionHandler<RequestType> exceptionHandler) {
        configuration.setExceptionHandler(validator.notNullField(exceptionHandler, "exceptionHandler"));
        return this;
    }

    @Override
    public void executeAsynchronous() {
        validator.validate();
        configuration.validateRequiredFields();
        GrpcCommunicationAsyncExecutor.execute(configuration);
    }

    @Override
    public <RequestType> void executeAsynchronous(RequestType request) {
        configuration.setRequest(validator.notNullField(request, "request"));
        validator.validate();
        configuration.validateRequiredFields();
        GrpcCommunicationAsyncExecutor.execute(configuration);
    }
}