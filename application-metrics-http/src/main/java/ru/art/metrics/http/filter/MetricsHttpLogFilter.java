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

package ru.art.metrics.http.filter;

import org.zalando.logbook.Logbook;
import org.zalando.logbook.LogbookCreator.Builder;
import ru.art.http.logger.ZalangoLogbookLogWriter;
import static org.zalando.logbook.Conditions.exclude;
import static org.zalando.logbook.Conditions.requestTo;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;
import static ru.art.metrics.constants.MetricsModuleConstants.METRICS_PATH;

public interface MetricsHttpLogFilter {
    static Builder logbookWithoutMetricsLogs(Builder builder) {
        return builder
                .condition(exclude(requestTo(httpServerModule().getPath() + METRICS_PATH)))
                .writer(new ZalangoLogbookLogWriter());
    }

    static Builder logbookWithoutMetricsLogs() {
        return Logbook.builder()
                .condition(exclude(requestTo(httpServerModule().getPath() + METRICS_PATH)))
                .writer(new ZalangoLogbookLogWriter());
    }
}
