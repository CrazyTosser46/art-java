package ru.art.http.server.specification;

import lombok.Getter;
import ru.art.entity.mapper.ValueToModelMapper.StringParametersMapToModelMapper;
import ru.art.http.server.model.HttpService;
import ru.art.service.ServiceLoggingInterception;
import ru.art.service.exception.UnknownServiceMethodException;
import ru.art.service.interceptor.ServiceExecutionInterceptor.ServiceResponseInterceptor;
import ru.art.service.model.ServiceInterceptionResult;
import ru.art.service.model.ServiceRequest;
import ru.art.service.model.ServiceResponse;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import static ru.art.entity.CollectionValuesFactory.byteCollection;
import static ru.art.entity.PrimitiveMapping.stringMapper;
import static ru.art.http.constants.MimeToContentTypeMapper.imagePng;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.HttpParameters.RESOURCE;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.HttpPath.IMAGE_PATH;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.Methods.IMAGE;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.Methods.RENDER;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.SERVICE_ID;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.WEB_RESOURCE;
import static ru.art.http.server.extractor.HttpWebResponseContentTypeExtractor.extractTypeByFile;
import static ru.art.http.server.interceptor.HttpServerInterception.interceptAndContinue;
import static ru.art.http.server.interceptor.HttpServerInterceptor.intercept;
import static ru.art.http.server.model.HttpService.httpService;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;
import static ru.art.http.server.service.HttpWebResourceService.getBinaryResource;
import static ru.art.http.server.service.HttpWebResourceService.getStringResource;
import static ru.art.service.interceptor.ServiceExecutionInterceptor.interceptResponse;
import static ru.art.service.model.ServiceInterceptionResult.nextInterceptor;
import java.util.List;

@Getter
public class HttpWebUiServiceSpecification implements HttpServiceSpecification {
    private final String serviceId = SERVICE_ID;
    private final HttpService httpService = httpService()

            .get(RENDER)
            .fromPathParameters(RESOURCE)
            .requestMapper((StringParametersMapToModelMapper<String>) value -> value.getParameter(RESOURCE))
            .overrideResponseContentType()
            .responseMapper(stringMapper.getFromModel())
            .addRequestInterceptor(intercept(interceptAndContinue(((request, response) -> response.setContentType(extractTypeByFile(request.getRequestURI()))))))
            .listen(httpServerModule().getPath())

            .get(IMAGE)
            .fromPathParameters(RESOURCE)
            .requestMapper((StringParametersMapToModelMapper<String>) value -> value.getParameter(RESOURCE))
            .produces(imagePng())
            .ignoreRequestAcceptType()
            .responseMapper(image -> byteCollection((byte[]) image))
            .listen(httpServerModule().getPath() + IMAGE_PATH)

            .serve(EMPTY_STRING);

    @Override
    public List<ServiceResponseInterceptor> getResponseInterceptors() {
        return linkedListOf(interceptResponse(new ServiceLoggingInterception() {
            @Override
            public ServiceInterceptionResult intercept(ServiceRequest<?> request, ServiceResponse<?> response) {
                super.intercept(request, ServiceResponse.builder()
                        .command(response.getCommand())
                        .serviceException(response.getServiceException())
                        .responseData(WEB_RESOURCE)
                        .build());
                return nextInterceptor(request, response);
            }
        }));
    }

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case RENDER:
                return cast(getStringResource(cast(request)));
            case IMAGE:
                return cast(getBinaryResource(cast(request)));
        }
        throw new UnknownServiceMethodException(serviceId, methodId);
    }
}