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

package io.art.http.server.filter;

import org.zalando.logbook.*;
import static io.art.core.checker.CheckerForEmptiness.*;
import static io.art.core.mime.MimeType.*;
import static io.art.http.server.HttpServerModuleConfiguration.*;
import static io.art.http.server.module.HttpServerModule.*;

public interface HttpResourceLogsFilter {
    static String replaceResponseBody(RawHttpResponse response) {
        //Attention! Dont change this to EMPTY_STRING or something like it. Logbook check filter result and if it null, then filter is not applying.
        String contentType = response.getContentType();
        if (isEmpty(contentType)) {
            return null;
        }
        return httpServerModule()
                .getResourceConfiguration()
                .getResourceExtensionMappings()
                .values()
                .stream()
                .filter(mapping -> valueOf(contentType).equals(mapping.getMimeType()))
                .findFirst()
                .map(HttpResourceExtensionMapping::getLogbookBodyReplacement)
                .orElse(null);
    }
}