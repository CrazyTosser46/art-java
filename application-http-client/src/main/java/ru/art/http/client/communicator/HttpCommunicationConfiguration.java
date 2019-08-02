package ru.art.http.client.communicator;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import ru.art.entity.Value;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.http.client.handler.HttpCommunicationCancellationHandler;
import ru.art.http.client.handler.HttpCommunicationExceptionHandler;
import ru.art.http.client.handler.HttpCommunicationResponseHandler;
import ru.art.http.client.interceptor.HttpClientInterceptor;
import ru.art.http.constants.HttpMethodType;
import ru.art.http.constants.MimeToContentTypeMapper;
import static lombok.AccessLevel.PACKAGE;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.http.client.module.HttpClientModule.httpClientModule;
import static ru.art.http.constants.HttpMethodType.GET;
import static ru.art.http.constants.MimeToContentTypeMapper.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Getter(value = PACKAGE)
@Setter(value = PACKAGE)
class HttpCommunicationConfiguration {
    private final List<String> pathParameters = linkedListOf();
    private final Map<String, String> queryParameters = mapOf();
    private final List<HttpClientInterceptor> requestInterceptors = linkedListOf();
    private final List<HttpClientInterceptor> responseInterceptors = linkedListOf();
    private final Map<String, String> headers = mapOf();
    private String url;
    private HttpMethodType methodType = GET;
    private ValueFromModelMapper<?, ? extends Value> requestMapper;
    private ValueToModelMapper<?, ? extends Value> responseMapper;
    private HttpCommunicationResponseHandler<?, ?> responseHandler;
    private HttpCommunicationExceptionHandler<?> exceptionHandler;
    private HttpCommunicationCancellationHandler<?> cancellationHandler;
    private Object request;
    private boolean chunkedBody;
    private boolean gzipCompressedBody;
    private RequestConfig requestConfig = httpClientModule().getRequestConfig();
    private HttpVersion httpProtocolVersion = httpClientModule().getHttpVersion();
    private MimeToContentTypeMapper producesContentType = all();
    private MimeToContentTypeMapper consumesContentType = all();
    private Charset requestContentCharset = contextConfiguration().getCharset();
    private String requestContentEncoding = contextConfiguration().getCharset().name();
    private boolean ignoreResponseContentType;
    private HttpClient syncClient;
    private HttpAsyncClient asyncClient;
}