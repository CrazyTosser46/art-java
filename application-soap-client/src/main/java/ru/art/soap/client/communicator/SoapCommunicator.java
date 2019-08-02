package ru.art.soap.client.communicator;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import ru.art.entity.XmlEntity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.http.client.handler.HttpCommunicationCancellationHandler;
import ru.art.http.client.handler.HttpCommunicationExceptionHandler;
import ru.art.http.client.handler.HttpCommunicationResponseHandler;
import ru.art.http.client.interceptor.HttpClientInterceptor;
import ru.art.http.client.model.HttpCommunicationTargetConfiguration;
import ru.art.soap.content.mapper.SoapMimeToContentTypeMapper;
import java.nio.charset.Charset;
import java.util.Optional;

public interface SoapCommunicator {
    static SoapCommunicator soapCommunicator(String endpointUrl) {
        return new SoapCommunicatorImplementation(endpointUrl);
    }

    static SoapCommunicator soapCommunicator(HttpCommunicationTargetConfiguration targetConfiguration) {
        return new SoapCommunicatorImplementation(targetConfiguration);
    }

    SoapCommunicator client(HttpClient syncClient);

    SoapCommunicator operationId(String operationId);

    SoapCommunicator requestConfig(RequestConfig requestConfig);

    SoapCommunicator requestMapper(ValueFromModelMapper<?, XmlEntity> requestMapper);

    SoapCommunicator responseMapper(ValueToModelMapper<?, XmlEntity> responseMapper);

    SoapCommunicator envelopeNamespace(String prefix, String namespace);

    SoapCommunicator bodyNamespace(String prefix, String namespace);

    SoapCommunicator operationNamespace(String prefix, String namespace);

    SoapCommunicator consumes(SoapMimeToContentTypeMapper soapMimeType);

    SoapCommunicator produces(SoapMimeToContentTypeMapper soapMimeType);

    SoapCommunicator requestCharset(Charset charset);

    SoapCommunicator requestBodyEncoding(String encoding);

    SoapCommunicator withRequestInterceptor(HttpClientInterceptor interceptor);

    SoapCommunicator withResponseInterceptor(HttpClientInterceptor interceptor);

    SoapCommunicator version(HttpVersion httpVersion);

    <RequestType, ResponseType> Optional<ResponseType> execute(RequestType request);

    SoapAsynchronousCommunicator asynchronous();

    interface SoapAsynchronousCommunicator {
        SoapAsynchronousCommunicator client(HttpAsyncClient asyncClient);

        <RequestType, ResponseType> SoapAsynchronousCommunicator responseHandler(HttpCommunicationResponseHandler<RequestType, ResponseType> handler);

        <RequestType> SoapAsynchronousCommunicator exceptionHandler(HttpCommunicationExceptionHandler<RequestType> handler);

        <RequestType> SoapAsynchronousCommunicator cancellationHandler(HttpCommunicationCancellationHandler<RequestType> handler);

        <RequestType> void executeAsynchronous(RequestType request);
    }
}