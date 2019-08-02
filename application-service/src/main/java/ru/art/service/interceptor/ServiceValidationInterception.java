/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.service.interceptor;

import ru.art.service.exception.ValidationException;
import ru.art.service.model.ServiceInterceptionResult;
import ru.art.service.model.ServiceRequest;
import ru.art.service.validation.Validatable;
import static java.util.Objects.isNull;
import static ru.art.service.ServiceModule.serviceModule;
import static ru.art.service.constants.RequestValidationPolicy.NON_VALIDATABLE;
import static ru.art.service.constants.RequestValidationPolicy.NOT_NULL;
import static ru.art.service.constants.ServiceExceptionsMessages.REQUEST_DATA_IS_NULL;
import static ru.art.service.model.ServiceInterceptionResult.nextInterceptor;

public class ServiceValidationInterception implements ServiceRequestInterception {
    @Override
    @SuppressWarnings("Duplicates")
    public ServiceInterceptionResult intercept(ServiceRequest<?> request) {
        if (request.getValidationPolicy() == NOT_NULL) {
            if (isNull(request.getRequestData())) {
                throw new ValidationException(REQUEST_DATA_IS_NULL);
            }
            return nextInterceptor(request);
        }
        if (request.getValidationPolicy() == NON_VALIDATABLE) return nextInterceptor(request);
        if (isNull(request.getRequestData())) {
            throw new ValidationException(REQUEST_DATA_IS_NULL);
        }
        ((Validatable) request.getRequestData()).onValidating(serviceModule().getValidator());
        return nextInterceptor(request);
    }
}
