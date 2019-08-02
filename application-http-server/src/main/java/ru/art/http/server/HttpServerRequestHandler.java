package ru.art.http.server;

import lombok.NoArgsConstructor;
import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.http.constants.HttpRequestDataSource;
import ru.art.http.mapper.HttpContentMapper;
import ru.art.http.mime.MimeType;
import ru.art.http.server.exception.HttpServerException;
import ru.art.http.server.model.HttpService.HttpMethod;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceRequest;
import ru.art.service.model.ServiceResponse;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.extension.InputStreamExtensions.toByteList;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.entity.Entity.EntityBuilder;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.http.server.body.descriptor.HttpBodyDescriptor.readRequestBody;
import static ru.art.http.server.constants.HttpServerExceptionMessages.*;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;
import static ru.art.http.server.module.HttpServerModule.httpServerModuleState;
import static ru.art.http.server.parser.HttpParametersParser.parsePathParameters;
import static ru.art.http.server.parser.HttpParametersParser.parseQueryParameters;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.service.ServiceController.executeServiceMethodUnchecked;
import static ru.art.service.factory.ServiceRequestFactory.newServiceRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = PRIVATE)
class HttpServerRequestHandler {
    static byte[] executeHttpService(ServiceMethodCommand command, HttpServletRequest request, HttpMethod httpMethod) {
        HttpRequestDataSource requestDataResource = httpMethod.getRequestDataSource();
        ServiceRequest<?> serviceRequest = isNull(requestDataResource) ?
                newServiceRequest(command) :
                createServiceRequest(command, request, httpMethod);
        switch (httpMethod.getResponseHandlingMode()) {
            case UNCHECKED:
                return mapServiceResponse(httpMethod, executeServiceMethodUnchecked(serviceRequest));
            case CHECKED:
                return mapExtractedServiceResponse(httpMethod, executeServiceMethodUnchecked(serviceRequest));
        }
        throw new HttpServerException(HTTP_RESPONSE_MODE_IS_NULL);
    }

    private static byte[] mapExtractedServiceResponse(HttpMethod httpMethod, ServiceResponse<?> response) {
        if (isNull(response.getServiceException())) {
            Object responseData = response.getResponseData();
            if (isNull(responseData)) return EMPTY_BYTES;
            ValueFromModelMapper responseMapper;
            if (isNull(responseMapper = httpMethod.getResponseMapper())) return EMPTY_BYTES;
            return mapResponseObject(responseData, cast(responseMapper));
        }
        ValueFromModelMapper exceptionMapper;
        if (isNull(exceptionMapper = httpMethod.getExceptionMapper())) return EMPTY_BYTES;
        return mapResponseObject(response.getServiceException(), cast(exceptionMapper));
    }

    private static byte[] mapServiceResponse(HttpMethod httpMethod, ServiceResponse<?> response) {
        return mapResponseObject(response, cast(httpMethod.getResponseMapper()));
    }

    private static byte[] mapResponseObject(Object object, ValueFromModelMapper<?, ? extends Value> mapper) {
        MimeType responseContentType = httpServerModuleState().getRequestContext().getAcceptType();
        HttpContentMapper contentMapper = httpServerModule().getContentMappers().get(responseContentType);
        Value responseEntity = mapper.map(cast(object));
        if (isNull(responseEntity)) return EMPTY_BYTES;
        return contentMapper.getToContent().mapToBytes(responseEntity, responseContentType, httpServerModuleState().getRequestContext().getAcceptCharset());
    }

    private static ServiceRequest<?> createServiceRequest(ServiceMethodCommand command, HttpServletRequest httpRequest, HttpMethod methodConfig) {
        Object requestData = parseRequestData(httpRequest, methodConfig);
        return newServiceRequest(command, requestData, methodConfig.getRequestValidationPolicy());
    }

    private static Object parseRequestData(HttpServletRequest request, HttpMethod methodConfig) {
        MimeType requestContentType = httpServerModuleState().getRequestContext().getContentType();
        ValueToModelMapper<?, ? extends Value> requestMapper = cast(methodConfig.getRequestMapper());
        switch (methodConfig.getRequestDataSource()) {
            case BODY:
                if (httpServerModuleState().getRequestContext().isHasContent()) return null;
                HttpContentMapper contentMapper = httpServerModule().getContentMappers().get(requestContentType);
                Value entity = contentMapper.getFromContent().mapFromBytes(readRequestBody(request), requestContentType, getOrElse(requestContentType.getCharset(), contextConfiguration().getCharset()));
                if (isNull(entity)) return null;
                return requestMapper.map(cast(entity));
            case PATH_PARAMETERS:
                return requestMapper.map(cast(parsePathParameters(request, methodConfig)));
            case QUERY_PARAMETERS:
                return requestMapper.map(cast(parseQueryParameters(request)));
            case MULTIPART:
                Entity multipartEntity = readMultiParts(request);
                if (isNull(multipartEntity) || multipartEntity.isEmpty()) return null;
                return requestMapper.map(cast(multipartEntity));
            default:
                throw new HttpServerException(format(UNKNOWN_HTTP_REQUEST_DATA_SOURCE, methodConfig.getRequestDataSource()));
        }
    }

    private static Entity readMultiParts(HttpServletRequest request) {
        if (httpServerModuleState().getRequestContext().isHasContent()) return null;
        Collection<Part> parts;
        try {
            parts = request.getParts();
        } catch (Exception e) {
            throw new HttpServerException(e);
        }
        EntityBuilder entityBuilder = entityBuilder();
        for (Part part : parts) {
            try {
                String submittedFileName = part.getSubmittedFileName();
                if (isEmpty(submittedFileName)) {
                    continue;
                }
                InputStream is = part.getInputStream();
                List<Byte> value = toByteList(is);
                if (!isEmpty(value)) {
                    entityBuilder.byteCollectionField(submittedFileName, value);
                }
            } catch (IOException e) {
                loggingModule()
                        .getLogger(HttpServerRequestHandler.class)
                        .warn(EXCEPTION_OCCURRED_DURING_READING_PART, e);
            }
        }
        return entityBuilder.build();
    }
}