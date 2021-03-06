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

import ru.art.core.constants.*;
import ru.art.http.server.interceptor.*;
import ru.art.logging.*;
import static java.lang.System.*;
import static java.util.UUID.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.InterceptionStrategy.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.*;
import static ru.art.logging.LoggingParametersManager.*;
import javax.servlet.http.*;
import java.util.*;

public class HttpServerTracingIdentifierInterception implements HttpServerInterception {
    @Override
    public InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> traceIdHeader = request.getHeaders(TRACE_ID_HEADER);
        Enumeration<String> profileHeader = request.getHeaders(PROFILE_HEADER);
        clearProtocolLoggingParameters();
        putProtocolCallLoggingParameters(ProtocolCallLoggingParameters.builder()
                .protocol(request.getScheme())
                .requestId(randomUUID().toString())
                .traceId(isEmpty(traceIdHeader) || !traceIdHeader.hasMoreElements() ? randomUUID().toString() : traceIdHeader.nextElement())
                .profile(extractProfile(profileHeader))
                .environment(getProperty(ENVIRONMENT_PROPERTY))
                .build());
        return NEXT_INTERCEPTOR;
    }

    private String extractProfile(Enumeration<String> profileHeaders) {
        if (isNotEmpty(profileHeaders) && profileHeaders.hasMoreElements()) {
            return profileHeaders.nextElement();
        }
        return EMPTY_STRING;
    }
}
