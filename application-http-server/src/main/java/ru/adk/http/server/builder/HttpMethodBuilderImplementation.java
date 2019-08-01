package ru.adk.http.server.builder;

import lombok.RequiredArgsConstructor;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.http.constants.HttpMethodType;
import ru.adk.http.constants.HttpRequestDataSource;
import ru.adk.http.constants.MimeToContentTypeMapper;
import ru.adk.http.server.builder.HttpServiceBuilder.*;
import ru.adk.http.server.exception.HttpServerException;
import ru.adk.http.server.interceptor.HttpServerInterceptor;
import ru.adk.http.server.model.HttpService;
import ru.adk.http.server.path.HttpPath;
import ru.adk.service.constants.RequestValidationPolicy;
import static java.util.Collections.emptySet;
import static java.util.Objects.isNull;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.StringConstants.SLASH;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import static ru.adk.core.factory.CollectionsFactory.linkedListOf;
import static ru.adk.core.factory.CollectionsFactory.setOf;
import static ru.adk.http.constants.HttpExceptionsMessages.*;
import static ru.adk.http.constants.HttpRequestDataSource.*;
import static ru.adk.http.server.constants.HttpServerExceptionMessages.HTTP_METHOD_LISTENING_PATH_IS_EMPTY;
import static ru.adk.service.constants.RequestValidationPolicy.NON_VALIDATABLE;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class HttpMethodBuilderImplementation implements HttpMethodBuilder,
        HttpMethodWithParamsBuilder,
        HttpMethodWithBodyBuilder,
        HttpMethodResponseBuilder,
        HttpMethodRequestBuilder {
    private final HttpServiceBuilderImplementation serviceConfigBuilder;
    private final HttpMethodType type;
    private final String methodId;
    private final List<HttpServerInterceptor> requestInterceptors = linkedListOf();
    private final List<HttpServerInterceptor> responseInterceptors = linkedListOf();
    private HttpRequestDataSource requestDataResource;
    private ValueToModelMapper requestMapper;
    private ValueFromModelMapper responseMapper;
    private ValueFromModelMapper exceptionMapper;
    private Set<String> pathParams;
    private RequestValidationPolicy requestValidationPolicy;
    private MimeToContentTypeMapper consumesContentType;
    private MimeToContentTypeMapper producesMimeType;
    private boolean ignoreRequestAcceptType;
    private boolean ignoreRequestContentType;
    private boolean overrideResponseContentType;

    @Override
    public HttpMethodBuilder addRequestInterceptor(HttpServerInterceptor interceptor) {
        if (isNull(interceptor)) throw new HttpServerException(REQUEST_INTERCEPTOR_IS_NULL);
        requestInterceptors.add(interceptor);
        return this;
    }

    @Override
    public HttpMethodBuilder addResponseInterceptor(HttpServerInterceptor interceptor) {
        if (isNull(interceptor)) throw new HttpServerException(RESPONSE_INTERCEPTOR_IS_NULL);
        responseInterceptors.add(interceptor);
        return this;
    }

    @Override
    public HttpServiceBuilder listen(String path) {
        if (isEmpty(path))
            throw new HttpServerException(HTTP_METHOD_LISTENING_PATH_IS_EMPTY);
        return serviceConfigBuilder.add(HttpService.HttpMethod.builder()
                .methodId(methodId)
                .path(HttpPath.builder().contextPath(path)
                        .pathParameters(getOrElse(pathParams, emptySet()))
                        .build())
                .methodType(type)
                .requestDataSource(requestDataResource)
                .requestMapper(requestMapper)
                .responseMapper(responseMapper)
                .exceptionMapper(exceptionMapper)
                .requestInterceptors(requestInterceptors)
                .responseInterceptors(responseInterceptors)
                .requestValidationPolicy(getOrElse(requestValidationPolicy, NON_VALIDATABLE))
                .consumesContentType(consumesContentType)
                .producesContentType(producesMimeType)
                .ignoreRequestAcceptType(ignoreRequestAcceptType)
                .ignoreRequestContentType(ignoreRequestContentType)
                .overrideResponseContentType(overrideResponseContentType)
                .build());
    }

    @Override
    public HttpServiceBuilder listen() {
        return listen(SLASH + methodId);
    }

    @Override
    public HttpMethodRequestBuilder fromQueryParameters() {
        requestDataResource = QUERY_PARAMETERS;
        return this;
    }

    @Override
    public HttpMethodRequestBuilder fromPathParameters(String... parameters) {
        requestDataResource = PATH_PARAMETERS;
        pathParams = setOf(parameters);
        return this;
    }

    @Override
    public HttpMethodRequestBuilder fromBody() {
        requestDataResource = BODY;
        return this;
    }

    @Override
    public HttpMethodRequestBuilder fromMultipart() {
        requestDataResource = MULTIPART;
        return this;
    }

    @Override
    public HttpMethodWithBodyBuilder consumes(MimeToContentTypeMapper mimeType) {
        if (isNull(mimeType)) throw new HttpServerException(REQUEST_CONTENT_TYPE_IS_NULL);
        this.consumesContentType = mimeType;
        return this;
    }

    @Override
    public HttpMethodResponseBuilder ignoreRequestAcceptType() {
        this.ignoreRequestAcceptType = true;
        return this;
    }

    @Override
    public HttpMethodResponseBuilder overrideResponseContentType() {
        this.overrideResponseContentType = true;
        return this;
    }

    @Override
    public HttpMethodResponseBuilder produces(MimeToContentTypeMapper mimeType) {
        if (isNull(mimeType)) throw new HttpServerException(RESPONSE_CONTENT_TYPE_IS_NULL);
        this.producesMimeType = mimeType;
        return this;
    }

    @Override
    public HttpMethodWithBodyBuilder ignoreRequestContentType() {
        ignoreRequestContentType = true;
        return this;
    }

    @Override
    public HttpMethodResponseBuilder requestMapper(ValueToModelMapper requestMapper) {
        if (isNull(requestMapper)) throw new HttpServerException(REQUEST_MAPPER_IS_NULL);
        this.requestMapper = requestMapper;
        return this;
    }

    @Override
    public HttpMethodRequestBuilder validationPolicy(RequestValidationPolicy policy) {
        if (isNull(policy)) throw new HttpServerException(VALIDATION_POLICY_IS_NULL);
        this.requestValidationPolicy = policy;
        return this;
    }

    @Override
    public HttpMethodBuilder responseMapper(ValueFromModelMapper responseMapper) {
        if (isNull(responseMapper)) throw new HttpServerException(RESPONSE_MAPPER_IS_NULL);
        this.responseMapper = responseMapper;
        return this;
    }

    public HttpMethodBuilder exceptionMapper(ValueFromModelMapper exceptionMapper) {
        if (isNull(exceptionMapper)) throw new HttpServerException(EXCEPTION_MAPPER_IS_NULL);
        this.exceptionMapper = exceptionMapper;
        return this;
    }
}
