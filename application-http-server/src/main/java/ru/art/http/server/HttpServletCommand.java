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

package ru.art.http.server;

import lombok.*;
import ru.art.http.constants.*;
import ru.art.http.server.model.*;

@Getter
@Builder
class HttpServletCommand {
    private final String path;
    private final MimeToContentTypeMapper consumesMimeType;
    private final MimeToContentTypeMapper producesMimeType;
    private final boolean ignoreRequestContentType;
    private final boolean ignoreRequestAcceptType;
    private final HttpService.HttpMethod httpMethod;
    private final String serviceId;
}
