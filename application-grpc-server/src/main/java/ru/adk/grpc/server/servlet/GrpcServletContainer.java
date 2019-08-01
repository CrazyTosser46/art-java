package ru.adk.grpc.server.servlet;

import io.grpc.stub.StreamObserver;
import lombok.Getter;
import ru.adk.entity.Value;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.grpc.server.exception.GrpcServletException;
import ru.adk.grpc.server.model.GrpcService.GrpcMethod;
import ru.adk.grpc.server.specification.GrpcServiceSpecification;
import ru.adk.grpc.servlet.GrpcRequest;
import ru.adk.grpc.servlet.GrpcResponse;
import ru.adk.grpc.servlet.GrpcServlet;
import ru.adk.logging.ServiceCallLoggingParameters;
import ru.adk.protobuf.entity.ProtobufValueMessage.ProtobufValue;
import ru.adk.service.exception.ServiceExecutionException;
import ru.adk.service.model.ServiceMethodCommand;
import ru.adk.service.model.ServiceRequest;
import ru.adk.service.model.ServiceResponse;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.ThreadContext.get;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.constants.StringConstants.BRACKETS;
import static ru.adk.core.constants.StringConstants.DOT;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import static ru.adk.core.extension.StringExtensions.emptyIfNull;
import static ru.adk.grpc.server.constants.GrpcServerExceptionMessages.*;
import static ru.adk.grpc.server.constants.GrpcServerLoggingMessages.GRPC_LOGGING_EVENT;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.RESPONSE_OK;
import static ru.adk.grpc.servlet.GrpcResponse.newBuilder;
import static ru.adk.logging.LoggingModule.loggingModule;
import static ru.adk.logging.LoggingModuleConstants.DEFAULT_REQUEST_ID;
import static ru.adk.logging.LoggingModuleConstants.LoggingParameters.REQUEST_ID_KEY;
import static ru.adk.logging.LoggingParametersManager.clearServiceCallLoggingParameters;
import static ru.adk.logging.LoggingParametersManager.putServiceCallLoggingParameters;
import static ru.adk.protobuf.descriptor.ProtobufEntityReader.readProtobuf;
import static ru.adk.protobuf.descriptor.ProtobufEntityWriter.writeProtobuf;
import static ru.adk.service.ServiceController.executeServiceMethodUnchecked;
import static ru.adk.service.constants.RequestValidationPolicy.NON_VALIDATABLE;
import static ru.adk.service.factory.ServiceRequestFactory.newServiceRequest;
import java.util.Map;

public class GrpcServletContainer extends GrpcServlet {
    @Getter
    private final GrpcServletImplementation servlet;

    public GrpcServletContainer(String path, Map<String, GrpcServiceSpecification> services) {
        servlet = new GrpcServletImplementation(path, services);
    }

    public class GrpcServletImplementation extends GrpcServletImplBase {
        private final Map<String, GrpcServiceSpecification> services;

        private GrpcServletImplementation(String path, Map<String, GrpcServiceSpecification> services) {
            super(path);
            this.services = services;
        }

        @Override
        public void executeService(GrpcRequest GrpcRequest, StreamObserver<GrpcResponse> responseObserver) {
            String serviceMethodId = GrpcRequest.getServiceId() + DOT + GrpcRequest.getMethodId() + BRACKETS;
            clearServiceCallLoggingParameters();
            putServiceCallLoggingParameters(ServiceCallLoggingParameters.builder()
                    .serviceId(GrpcRequest.getServiceId())
                    .serviceMethodId(serviceMethodId)
                    .serviceMethodCommand(serviceMethodId + DOT + getOrElse(get(REQUEST_ID_KEY), DEFAULT_REQUEST_ID))
                    .loggingEventType(GRPC_LOGGING_EVENT)
                    .build());
            try {
                executeServiceChecked(GrpcRequest, responseObserver);
                clearServiceCallLoggingParameters();
            } catch (Exception e) {
                loggingModule()
                        .getLogger(GrpcServletContainer.class)
                        .error(GRPC_SERVICE_EXCEPTION, e);
                responseObserver.onNext(newBuilder()
                        .setErrorCode(GRPC_SERVLET_ERROR)
                        .setErrorMessage(getOrElse(e.getMessage(), e.getClass().toString()))
                        .build());
                responseObserver.onCompleted();
                clearServiceCallLoggingParameters();
            }
        }

        void executeServiceChecked(GrpcRequest grpcRequest, StreamObserver<GrpcResponse> responseObserver) {
            if (isNull(grpcRequest) || isNull(responseObserver)) {
                throw new GrpcServletException(GRPC_SERVLET_INPUT_PARAMETERS_NULL);
            }
            GrpcServiceSpecification service = services.get(grpcRequest.getServiceId());
            if (isNull(service)) {
                sendServiceNotExistsError(responseObserver, grpcRequest.getServiceId());
                return;
            }
            if (!service.getServiceTypes().contains(GRPC_SERVICE_TYPE)) {
                sendServiceNotExistsError(responseObserver, grpcRequest.getServiceId());
                return;
            }
            Value requestData = readProtobuf(grpcRequest.getRequestData());
            Map<String, GrpcMethod> methodsConfig = service.getGrpcService().getMethods();
            GrpcMethod methodConfig = methodsConfig.get(grpcRequest.getMethodId());
            if (isNull(methodConfig)) {
                sendMethodNotExistsError(responseObserver, grpcRequest.getMethodId());
                return;
            }
            ServiceRequest<?> serviceRequest = buildServiceRequest(grpcRequest, requestData, methodConfig);
            ServiceResponse<?> response = executeServiceMethodUnchecked(serviceRequest);
            ServiceExecutionException serviceException = response.getServiceException();
            ValueFromModelMapper<?, ? extends Value> responseMapper = cast(methodConfig.responseMapper());
            if (nonNull(serviceException)) {
                loggingModule()
                        .getLogger(GrpcServletContainer.class)
                        .error(GRPC_SERVICE_EXCEPTION, serviceException);
                GrpcResponse.Builder builder = newBuilder()
                        .setErrorCode(getOrElse(serviceException.getErrorCode(), GRPC_SERVLET_ERROR))
                        .setErrorMessage(emptyIfNull(serviceException.getErrorMessage()));
                if (nonNull(responseMapper)) {
                    builder.setResponseData(writeProtobuf(responseMapper.map(cast(response.getResponseData()))));
                }
                GrpcResponse GrpcResponse = builder.build();
                responseObserver.onNext(GrpcResponse);
                responseObserver.onCompleted();
                return;
            }
            if (isNull(responseMapper)) {
                responseObserver.onNext(newBuilder().setErrorCode(RESPONSE_OK).build());
                responseObserver.onCompleted();
                return;
            }
            ProtobufValue responseDataValue = writeProtobuf(responseMapper.map(cast(response.getResponseData())));
            responseObserver.onNext(newBuilder().setErrorCode(RESPONSE_OK).setResponseData(responseDataValue).build());
            responseObserver.onCompleted();
        }

        private void sendServiceNotExistsError(StreamObserver<GrpcResponse> responseObserver, String serviceId) {
            loggingModule()
                    .getLogger(GrpcServletContainer.class)
                    .error(format(GRPC_SERVICE_NOT_EXISTS_MESSAGE, serviceId));
            responseObserver.onNext(newBuilder().setErrorCode(GRPC_SERVICE_NOT_EXISTS_CODE).setErrorMessage(format(GRPC_SERVICE_NOT_EXISTS_MESSAGE, serviceId)).build());
            responseObserver.onCompleted();
        }

        private void sendMethodNotExistsError(StreamObserver<GrpcResponse> responseObserver, String methodId) {
            String message = format(GRPC_METHOD_NOT_EXISTS_MESSAGE, methodId);
            loggingModule()
                    .getLogger(GrpcServletContainer.class)
                    .error(message);
            responseObserver.onNext(newBuilder().setErrorCode(GRPC_METHOD_NOT_EXISTS_CODE).setErrorMessage(message).build());
            responseObserver.onCompleted();
        }

        private ServiceRequest<?> buildServiceRequest(GrpcRequest GrpcRequest, Value requestData, GrpcMethod methodConfig) {
            ServiceMethodCommand serviceMethodCommand = new ServiceMethodCommand(GrpcRequest.getServiceId(), GrpcRequest.getMethodId());
            if (isNull(requestData)) {
                return newServiceRequest(serviceMethodCommand);
            }

            ValueToModelMapper<?, ? extends Value> requestMapper = cast(methodConfig.requestMapper());
            if (isNull(requestMapper)) {
                return newServiceRequest(serviceMethodCommand);
            }

            Object request = requestMapper.map(cast(requestData));
            return newServiceRequest(serviceMethodCommand, request, getOrElse(methodConfig.validationPolicy(), NON_VALIDATABLE));
        }
    }
}