/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.server.service.model;

import com.google.common.collect.*;
import io.art.core.module.*;
import io.art.resilience.model.*;
import lombok.*;
import static com.google.common.collect.ImmutableMap.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.server.constants.ServerModuleConstants.ConfigurationKeys.*;
import static java.util.Optional.*;
import java.util.*;

@Getter
@AllArgsConstructor
public class ServiceConfiguration {
    private final boolean deactivated;
    private final ResilienceConfiguration resilienceConfiguration;
    private final ImmutableMap<String, ServiceMethodConfiguration> methods;

    public static ServiceConfiguration from(ModuleConfigurationSource source) {
        boolean deactivated = getOrElse(source.getBool(DEACTIVATED_KEY), false);
        ResilienceConfiguration resilience = let(source.getInner(RESILIENCE_KEY), ResilienceConfiguration::from);
        ImmutableMap<String, ServiceMethodConfiguration> methods = ofNullable(source.getInnerMap(METHODS_KEY))
                .map(configurations -> configurations.entrySet()
                        .stream()
                        .collect(toImmutableMap(Map.Entry::getKey, entry -> ServiceMethodConfiguration.from(entry.getValue()))))
                .orElse(ImmutableMap.of());
        return new ServiceConfiguration(deactivated, resilience, methods);
    }
}