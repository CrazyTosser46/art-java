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

package ru.art.metrics.module;

import lombok.*;
import ru.art.core.module.*;
import ru.art.metrics.configuration.*;

import static lombok.AccessLevel.*;
import static ru.art.core.context.Context.*;
import static ru.art.metrics.constants.MetricsModuleConstants.*;

@Getter
public class MetricsModule implements Module<MetricModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final MetricModuleConfiguration metricsModule = context().getModule(METRICS_MODULE_ID, MetricsModule::new);
    private final String id = METRICS_MODULE_ID;
    private final MetricModuleConfiguration defaultConfiguration = MetricModuleConfiguration.DEFAULT_CONFIGURATION;

    public static MetricModuleConfiguration metricsModule() {
        if (insideDefaultContext()) {
            return MetricModuleConfiguration.DEFAULT_CONFIGURATION;
        }
        return getMetricsModule();
    }

    @Override
    public void onLoad() {
        metricsModule().getMeterBinders().forEach(meter -> meter.bindTo(metricsModule().getPrometheusMeterRegistry()));
    }
}
