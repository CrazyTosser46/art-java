package ru.art.service.mapping;

import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.service.constants.RequestValidationPolicy;
import ru.art.service.exception.ServiceMappingException;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceRequest;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.core.util.Assert.isEmpty;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.mapper.ValueMapper.mapper;
import static ru.art.service.constants.RequestValidationPolicy.NON_VALIDATABLE;
import static ru.art.service.constants.ServiceExceptionsMessages.*;

public interface ServiceRequestMapping {
    String SERVICE_METHOD_COMMAND = "serviceMethodCommand";
    String SERVICE_ID = "serviceId";
    String METHOD_ID = "methodId";
    String VALIDATION_POLICY = "validationPolicy";
    String REQUEST_DATA = "requestData";

    @SuppressWarnings("Duplicates")
    static <D> ValueToModelMapper.EntityToModelMapper<ServiceRequest<D>> toServiceRequest(final ValueToModelMapper<D, Value> requestDataMapper) {
        return value -> {
            Entity serviceMethodCommandEntity = value.getEntity(SERVICE_METHOD_COMMAND);
            if (isNull(serviceMethodCommandEntity)) throw new ServiceMappingException(SERVICE_COMMAND_IS_NULL);
            String serviceId = serviceMethodCommandEntity.getString(SERVICE_ID);
            if (isNull(serviceId)) throw new ServiceMappingException(SERVICE_ID_IS_NULL);
            String methodId = serviceMethodCommandEntity.getString(METHOD_ID);
            if (isNull(methodId)) throw new ServiceMappingException(METHOD_ID_IS_NULL);
            String validationPolicyAsString = value.getString(VALIDATION_POLICY);
            RequestValidationPolicy requestValidationPolicy = isEmpty(validationPolicyAsString) ? NON_VALIDATABLE : RequestValidationPolicy.valueOf(validationPolicyAsString);
            D requestData = isNull(requestDataMapper) ? null : value.getValue(REQUEST_DATA, requestDataMapper);
            return new ServiceRequest<>(new ServiceMethodCommand(serviceId, methodId), requestValidationPolicy, requestData);
        };
    }

    static <D> ValueFromModelMapper.EntityFromModelMapper<ServiceRequest<D>> fromServiceRequest(final ValueFromModelMapper<D, Value> requestDataMapper) {
        return model -> {
            ServiceMethodCommand serviceMethodCommand = model.getServiceMethodCommand();
            if (isNull(serviceMethodCommand)) throw new ServiceMappingException(SERVICE_COMMAND_IS_NULL);
            String serviceId = serviceMethodCommand.getServiceId();
            if (isNull(serviceId)) throw new ServiceMappingException(SERVICE_ID_IS_NULL);
            String methodId = serviceMethodCommand.getMethodId();
            if (isNull(methodId)) throw new ServiceMappingException(METHOD_ID_IS_NULL);
            RequestValidationPolicy validationPolicy = isNull(validationPolicy = model.getValidationPolicy()) ? NON_VALIDATABLE : validationPolicy;
            return entityBuilder()
                    .entityField(SERVICE_METHOD_COMMAND, entityBuilder()
                            .stringField(SERVICE_ID, serviceId)
                            .stringField(METHOD_ID, methodId)
                            .build())
                    .stringField(VALIDATION_POLICY, validationPolicy.toString())
                    .valueField(REQUEST_DATA, isNull(requestDataMapper) ? null : requestDataMapper.map(model.getRequestData()))
                    .build();
        };
    }

    static <D> ValueToModelMapper.EntityToModelMapper<ServiceRequest<D>> toServiceRequest() {
        return toServiceRequest(null);
    }

    static <D> ValueFromModelMapper.EntityFromModelMapper<ServiceRequest<D>> fromServiceRequest() {
        return fromServiceRequest(null);
    }

    static <V> ValueMapper<ServiceRequest<V>, Entity> serviceRequestMapper(ValueToModelMapper<V, Value> requestToModelMapper, ValueFromModelMapper<V, Value> requestFromModelMapper) {
        return mapper(fromServiceRequest(requestFromModelMapper), toServiceRequest(requestToModelMapper));
    }
}