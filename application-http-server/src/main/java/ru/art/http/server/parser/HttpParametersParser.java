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

package ru.art.http.server.parser;

import ru.art.core.factory.CollectionsFactory.MapBuilder;
import ru.art.entity.StringParametersMap;
import ru.art.http.server.model.HttpService;
import static java.util.stream.Collectors.toMap;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.StringConstants.SLASH;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

public interface HttpParametersParser {
    static StringParametersMap parseQueryParameters(HttpServletRequest request) {
        Map<String, String> parameters = request
                .getParameterMap()
                .keySet()
                .stream()
                .collect(toMap(name -> name, name -> request.getParameterValues(name)[0]));
        return StringParametersMap.builder()
                .parameters(parameters)
                .build();
    }

    static StringParametersMap parsePathParameters(HttpServletRequest request, HttpService.HttpMethod methodConfig) {
        String pathParameterString = request.getPathInfo();
        if (isEmpty(pathParameterString)) return StringParametersMap.builder().build();
        String[] pathParameterValues = pathParameterString.split(SLASH);
        if (isEmpty(pathParameterValues) || pathParameterValues.length == 1)
            return StringParametersMap.builder().build();
        Set<String> pathParameterNames = methodConfig.getPath().getPathParameters();
        int pathParameterIndex = 1;
        MapBuilder<String, String> pathParameters = mapOf();
        for (String name : pathParameterNames) {
            pathParameters.add(name, pathParameterValues[pathParameterIndex]);
            pathParameterIndex++;
        }
        return StringParametersMap.builder()
                .parameters(cast(pathParameters))
                .build();
    }
}

