package ru.adk.example.api.communication.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.adk.example.api.interceptor.http.ExampleHttpClientInterception;
import ru.adk.http.client.communicator.HttpCommunicator;
import ru.adk.http.client.specification.HttpCommunicationSpecification;
import ru.adk.service.exception.UnknownServiceMethodException;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.extension.OptionalExtensions.unwrap;
import static ru.adk.example.api.constants.ExampleApiConstants.EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID;
import static ru.adk.example.api.constants.ExampleApiConstants.EXAMPLE_SERVICE_ID;
import static ru.adk.example.api.constants.ExampleApiConstants.Methods.*;
import static ru.adk.example.api.constants.ExampleApiConstants.Paths.*;
import static ru.adk.example.api.mapping.ExampleRequestResponseMapper.ExampleRequestMapper.fromExampleRequest;
import static ru.adk.example.api.mapping.ExampleRequestResponseMapper.ExampleResponseMapper.toExampleResponse;
import static ru.adk.example.api.mapping.ExampleStateModelMapper.toExampleStateModel;
import static ru.adk.http.client.communicator.HttpCommunicator.httpCommunicator;
import static ru.adk.http.client.interceptor.HttpClientInterceptor.interceptRequest;
import static ru.adk.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;

/**
 * Http Communication specification is made for preparing http clients for calling external module
 * We set all needed mappers here, serviceId and methodId of external module http service
 */
@Getter
@RequiredArgsConstructor
public class ExampleServiceHttpCommunicationSpecification implements HttpCommunicationSpecification {
    private final String serviceId = EXAMPLE_HTTP_COMMUNICATION_SERVICE_ID;

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final HttpCommunicator requestResponseHandlingExample = httpCommunicator(communicationTarget(EXAMPLE_SERVICE_ID)
            .addPath(REQUEST_RESPONSE_HANDLING_EXAMPLE_PATH))
            .addRequestInterceptor(interceptRequest(new ExampleHttpClientInterception()))
            .consumes(applicationJsonUtf8())
            .responseMapper(toExampleResponse)
            .post()
            .requestMapper(fromExampleRequest)
            .produces(applicationJsonUtf8());

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final HttpCommunicator usingConfigurationValuesExample = httpCommunicator(communicationTarget(EXAMPLE_SERVICE_ID)
            .addPath(USING_CONFIGURATION_VALUES_EXAMPLE_PATH))
            .post();

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final HttpCommunicator soapClientExample = httpCommunicator(communicationTarget(EXAMPLE_SERVICE_ID)
            .addPath(SOAP_CLIENT_EXAMPLE_PATH))
            .post();

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final HttpCommunicator httpClientExample = httpCommunicator(communicationTarget(EXAMPLE_SERVICE_ID)
            .addPath(HTTP_CLIENT_EXAMPLE_PATH))
            .post();

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final HttpCommunicator protobufClientExample = httpCommunicator(communicationTarget(EXAMPLE_SERVICE_ID)
            .addPath(GRPC_CLIENT_EXAMPLE_PATH))
            .post();

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final HttpCommunicator sqlExample = httpCommunicator(communicationTarget(EXAMPLE_SERVICE_ID)
            .addPath(SQL_EXAMPLE_PATH))
            .post();

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final HttpCommunicator rocksDbExample = httpCommunicator(communicationTarget(EXAMPLE_SERVICE_ID)
            .addPath(ROCKS_DB_EXAMPLE_PATH))
            .post();

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final HttpCommunicator loggingExample = httpCommunicator(communicationTarget(EXAMPLE_SERVICE_ID)
            .addPath(LOGGING_EXAMPLE_PATH))
            .post();


    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final HttpCommunicator jsonReadWriteExample = httpCommunicator(communicationTarget(EXAMPLE_SERVICE_ID)
            .addPath(JSON_READ_WRITE_EXAMPLE_PATH))
            .post();

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final HttpCommunicator protobufReadWriteExample = httpCommunicator(communicationTarget(EXAMPLE_SERVICE_ID)
            .addPath(PROTOBUF_READ_WRITE_EXAMPLE_PATH))
            .post();


    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final HttpCommunicator asyncTaskExecutingExample = httpCommunicator(communicationTarget(EXAMPLE_SERVICE_ID)
            .addPath(ASYNC_TASK_EXECUTING_EXAMPLE_PATH))
            .post();

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final HttpCommunicator getExampleModuleState = httpCommunicator(communicationTarget(EXAMPLE_SERVICE_ID)
            .addPath(GET_EXAMPLE_MODULE_STATE_PATH))
            .consumes(applicationJsonUtf8())
            .responseMapper(toExampleStateModel)
            .post();

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case REQUEST_RESPONSE_HANDLING_EXAMPLE:
                return unwrap(cast(requestResponseHandlingExample().execute(request)));
            case USING_CONFIGURATION_VALUES_EXAMPLE:
                usingConfigurationValuesExample().execute();
                return null;
            case SOAP_CLIENT_EXAMPLE:
                soapClientExample().execute();
                return null;
            case HTTP_CLIENT_EXAMPLE:
                httpClientExample().execute();
                return null;
            case GRPC_CLIENT_EXAMPLE:
                protobufClientExample().execute();
                return null;
            case SQL_EXAMPLE:
                sqlExample().execute();
                return null;
            case ROCKS_DB_EXAMPLE:
                rocksDbExample().execute();
                return null;
            case LOGGING_EXAMPLE:
                loggingExample().execute();
                return null;
            case JSON_READ_WRITE_EXAMPLE:
                jsonReadWriteExample().execute();
                return null;
            case PROTOBUF_READ_WRITE_EXAMPLE:
                protobufReadWriteExample().execute();
                return null;
            case ASYNC_TASK_EXECUTING_EXAMPLE:
                asyncTaskExecutingExample().execute();
            case GET_EXAMPLE_MODULE_STATE:
                return unwrap(cast(getExampleModuleState().execute()));
        }
        throw new UnknownServiceMethodException(getServiceId(), methodId);
    }
}
