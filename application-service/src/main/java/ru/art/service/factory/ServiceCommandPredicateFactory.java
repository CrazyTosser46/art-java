/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.service.factory;

import lombok.experimental.*;
import ru.art.service.model.*;
import static java.util.Objects.nonNull;
import java.util.function.*;

@UtilityClass
public class ServiceCommandPredicateFactory {
    public static Predicate<ServiceMethodCommand> byServiceId(String id) {
        return command -> nonNull(command) && id.equalsIgnoreCase(command.getServiceId());
    }

    public static Predicate<ServiceMethodCommand> byServiceMethodId(String serviceId, String methodId) {
        return command -> nonNull(command) && new ServiceMethodCommand(serviceId, methodId).equals(command);
    }
}
