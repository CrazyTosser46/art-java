package ru.adk.http.client.communicator;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import ru.adk.core.validator.BuilderValidator;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.http.client.communicator.HttpCommunicator.HttpAsynchronousCommunicator;
import ru.adk.http.client.handler.HttpCommunicationCancellationHandler;
import ru.adk.http.client.handler.HttpCommunicationExceptionHandler;
import ru.adk.http.client.handler.HttpCommunicationResponseHandler;
import ru.adk.http.client.interceptor.HttpClientInterceptor;
import ru.adk.http.client.model.HttpCommunicationTargetConfiguration;
import ru.adk.http.constants.MimeToContentTypeMapper;
import static java.util.Optional.ofNullable;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.adk.core.constants.StringConstants.COLON;
import static ru.adk.core.constants.StringConstants.SCHEME_DELIMITER;
import static ru.adk.core.context.Context.contextConfiguration;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import static ru.adk.core.extension.StringExtensions.emptyIfNull;
import static ru.adk.http.client.communicator.HttpCommunicationExecutor.executeAsyncRequest;
import static ru.adk.http.client.communicator.HttpCommunicationExecutor.executeSyncRequest;
import static ru.adk.http.client.module.HttpClientModule.httpClientModule;
import static ru.adk.http.constants.HttpMethodType.*;
import java.nio.charset.Charset;
import java.util.Optional;

public class HttpCommunicatorImplementation implements HttpCommunicator, HttpAsynchronousCommunicator {
    private final BuilderValidator validator = new BuilderValidator(HttpCommunicator.class.getName());
    private final HttpCommunicationConfiguration configuration = new HttpCommunicationConfiguration();

    HttpCommunicatorImplementation(String url) {
        this(HttpCommunicationTargetConfiguration.builder().build().url(url));
    }

    HttpCommunicatorImplementation(HttpCommunicationTargetConfiguration targetConfiguration) {
        config(targetConfiguration.requestConfig());
        if (isNotEmpty(targetConfiguration.url())) {
            configuration.setUrl(targetConfiguration.url());
            return;
        }
        configuration.setUrl(validator.notEmptyField(targetConfiguration.scheme(), "scheme") + SCHEME_DELIMITER
                + validator.notEmptyField(targetConfiguration.host(), "host")
                + COLON + validator.notNullField(targetConfiguration.port(), "port")
                + validator.notEmptyField(targetConfiguration.path(), "path"));
    }

    @Override
    public HttpCommunicator get() {
        configuration.setMethodType(GET);
        return this;
    }

    @Override
    public HttpCommunicator post() {
        configuration.setMethodType(POST);
        return this;
    }

    @Override
    public HttpCommunicator put() {
        configuration.setMethodType(PUT);
        return this;
    }

    @Override
    public HttpCommunicator patch() {
        configuration.setMethodType(PATCH);
        return this;
    }

    @Override
    public HttpCommunicator options() {
        configuration.setMethodType(OPTIONS);
        return this;
    }

    @Override
    public HttpCommunicator delete() {
        configuration.setMethodType(DELETE);
        return this;
    }

    @Override
    public HttpCommunicator trace() {
        configuration.setMethodType(TRACE);
        return this;
    }

    @Override
    public HttpCommunicator head() {
        configuration.setMethodType(HEAD);
        return this;
    }

    @Override
    public HttpCommunicator produces(MimeToContentTypeMapper requestContentTypeMapper) {
        configuration.setProducesContentType(validator.notNullField(requestContentTypeMapper, "requestContentTypeMapper"));
        return this;
    }

    @Override
    public HttpCommunicator requestMapper(ValueFromModelMapper requestMapper) {
        configuration.setRequestMapper(validator.notNullField(cast(requestMapper), "requestMapper"));
        return this;
    }

    @Override
    public HttpCommunicator client(HttpClient client) {
        configuration.setSyncClient(validator.notNullField(client, "syncClient"));
        return this;
    }

    @Override
    public HttpCommunicator consumes(MimeToContentTypeMapper responseContentTypeMapper) {
        configuration.setConsumesContentType(validator.notNullField(responseContentTypeMapper, "responseContentTypeMapper"));
        return this;
    }

    @Override
    public HttpCommunicator ignoreResponseContentType() {
        configuration.setIgnoreResponseContentType(true);
        return this;
    }

    @Override
    public HttpCommunicator responseMapper(ValueToModelMapper responseMapper) {
        configuration.setResponseMapper(validator.notNullField(cast(responseMapper), "responseMapper"));
        return this;

    }

    @Override
    public HttpCommunicator config(RequestConfig requestConfig) {
        configuration.setRequestConfig(validator.notNullField(requestConfig, "requestConfig"));
        return this;
    }

    @Override
    public HttpCommunicator version(HttpVersion httpVersion) {
        HttpVersion version = getOrElse(httpVersion, httpClientModule().getHttpVersion());
        configuration.setHttpProtocolVersion(validator.notNullField(version, "httpVersion"));
        return this;
    }

    @Override
    public HttpCommunicator requestCharset(Charset requestContentCharset) {
        Charset charset = getOrElse(requestContentCharset, contextConfiguration().getCharset());
        configuration.setRequestContentCharset(validator.notNullField(charset, "requestContentCharset"));
        return this;
    }

    @Override
    public HttpCommunicator addPathParameter(String parameter) {
        configuration.getPathParameters().add(validator.notEmptyField(parameter, "pathParameter"));
        return this;
    }

    @Override
    public HttpCommunicator addRequestInterceptor(HttpClientInterceptor interceptor) {
        configuration.getRequestInterceptors().add(validator.notNullField(interceptor, "requestInterceptor"));
        return this;
    }

    @Override
    public HttpCommunicator addResponseInterceptor(HttpClientInterceptor interceptor) {
        configuration.getResponseInterceptors().add(validator.notNullField(interceptor, "responseInterceptor"));
        return this;
    }

    @Override
    public HttpCommunicator addQueryParameter(String name, String value) {
        configuration.getQueryParameters().put(validator.notEmptyField(name, "queryParameterName"), validator.notNullField(value, "queryParameterValue"));
        return this;
    }

    @Override
    public HttpCommunicator addHeader(String name, String value) {
        configuration.getHeaders().put(validator.notEmptyField(name, "headerName"), validator.notNullField(value, "headerValue"));
        return this;
    }

    @Override
    public HttpCommunicator chunked() {
        configuration.setChunkedBody(true);
        return this;
    }

    @Override
    public HttpCommunicator gzipCompressed() {
        configuration.setGzipCompressedBody(true);
        return this;
    }

    @Override
    public HttpCommunicator requestEncoding(String encoding) {
        configuration.setRequestContentEncoding(emptyIfNull(encoding));
        return this;
    }

    @Override
    public <RequestType, ResponseType> Optional<ResponseType> execute(RequestType request) {
        configuration.setRequest(validator.notNullField(request, "request"));
        validator.validate();
        return ofNullable(executeSyncRequest(configuration));
    }

    @Override
    public <ResponseType> Optional<ResponseType> execute() {
        validator.validate();
        return ofNullable(executeSyncRequest(configuration));
    }


    @Override
    public HttpAsynchronousCommunicator client(HttpAsyncClient client) {
        configuration.setAsyncClient(validator.notNullField(client, "asyncClient"));
        return this;
    }

    @Override
    public <RequestType, ResponseType> HttpAsynchronousCommunicator completionHandler(HttpCommunicationResponseHandler<RequestType, ResponseType> handler) {
        configuration.setResponseHandler(validator.notNullField(handler, "responseHandler"));
        return this;
    }

    @Override
    public <RequestType> HttpAsynchronousCommunicator exceptionHandler(HttpCommunicationExceptionHandler<RequestType> handler) {
        configuration.setExceptionHandler(validator.notNullField(handler, "exceptionHandler"));
        return this;
    }

    @Override
    public <RequestType> HttpAsynchronousCommunicator cancellationHandler(HttpCommunicationCancellationHandler<RequestType> handler) {
        configuration.setCancellationHandler(validator.notNullField(handler, "cancellationHandler"));
        return this;
    }

    @Override
    public <RequestType> void executeAsynchronous(RequestType request) {
        configuration.setRequest(validator.notNullField(request, "request"));
        validator.validate();
        executeAsyncRequest(configuration);
    }

    @Override
    public HttpAsynchronousCommunicator asynchronous() {
        return this;
    }

    @Override
    public void executeAsynchronous() {
        validator.validate();
        executeAsyncRequest(configuration);
    }
}