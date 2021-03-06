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

package ru.art.soap.server.configuration;

import ru.art.core.module.*;
import ru.art.entity.mapper.ValueFromModelMapper.*;
import ru.art.soap.server.model.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.soap.server.constans.SoapServerModuleConstants.ResponseFaultConstants.*;
import static ru.art.soap.server.mapper.SoapMapper.*;

public interface SoapServerModuleConfiguration extends ModuleConfiguration {
    <T extends Throwable> T getDefaultFaultResponse();

    <T extends Throwable> XmlEntityFromModelMapper<T> getDefaultFaultMapper();

    SoapServerModuleDefaultConfiguration DEFAULT_CONFIGURATION = new SoapServerModuleDefaultConfiguration();

    class SoapServerModuleDefaultConfiguration implements SoapServerModuleConfiguration {
        private SoapFault defaultFaultResponse = SoapFault.builder()
                .codeValue(UNEXPECTED_ERROR)
                .reasonText(UNEXPECTED_ERROR_TEXT)
                .build();
        private XmlEntityFromModelMapper<SoapFault> defaultFaultMapper = soapResponseFaultMapper;

        public <T extends Throwable> T getDefaultFaultResponse() {
            return cast(this.defaultFaultResponse);
        }

        public <T extends Throwable> XmlEntityFromModelMapper<T> getDefaultFaultMapper() {
            return cast(this.defaultFaultMapper);
        }
    }
}
