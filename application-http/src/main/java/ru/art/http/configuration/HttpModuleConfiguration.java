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

package ru.art.http.configuration;

import lombok.*;
import org.zalando.logbook.*;
import ru.art.core.mime.*;
import ru.art.core.module.*;
import ru.art.entity.Value;
import ru.art.entity.interceptor.*;
import ru.art.http.constants.*;
import ru.art.http.logger.*;
import ru.art.http.mapper.*;
import ru.art.logging.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.http.constants.HttpMimeTypes.*;
import static ru.art.http.constants.MimeToContentTypeMapper.*;
import java.util.*;

public interface HttpModuleConfiguration extends ModuleConfiguration {
    boolean isEnableRawDataTracing();

    boolean isEnableValueTracing();

    Map<MimeType, HttpContentMapper> getContentMappers();

    Logbook getLogbook();

    List<ValueInterceptor<Value, Value>> getRequestValueInterceptors();

    List<ValueInterceptor<Value, Value>> getResponseValueInterceptors();

    MimeToContentTypeMapper getConsumesMimeTypeMapper();

    MimeToContentTypeMapper getProducesMimeTypeMapper();

    @Getter
    class HttpModuleDefaultConfiguration implements HttpModuleConfiguration {
        private final boolean enableRawDataTracing = false;
        private final boolean enableValueTracing = false;
        private final boolean enableMetricsMonitoring = true;
        private final MimeToContentTypeMapper consumesMimeTypeMapper = all();
        private final MimeToContentTypeMapper producesMimeTypeMapper = all();
        private final HttpTextPlainMapper textPlainMapper = new HttpTextPlainMapper();
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final Map<MimeType, HttpContentMapper> contentMappers = mapOf(ALL, new HttpContentMapper(getTextPlainMapper(), getTextPlainMapper()));
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final List<ValueInterceptor<Value, Value>> requestValueInterceptors = initializeValueInterceptors();
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final List<ValueInterceptor<Value, Value>> responseValueInterceptors = initializeValueInterceptors();
        @Getter(lazy = true)
        private final Logbook logbook = Logbook.builder().writer(new ZalangoLogbookLogWriter(this::isEnableRawDataTracing)).build();

        private List<ValueInterceptor<Value, Value>> initializeValueInterceptors() {
            return linkedListOf(new LoggingValueInterceptor<>(this::isEnableValueTracing));
        }
    }
}