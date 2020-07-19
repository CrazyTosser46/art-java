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

package io.art.server.model;

import io.github.resilience4j.ratelimiter.*;
import lombok.*;
import lombok.experimental.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RateLimiterServiceConfig {
    private boolean limited;
    private RateLimiterConfig rateLimiterConfig;

    public static RateLimiterServiceConfigBuilder builder() {
        return new RateLimiterServiceConfigBuilder();
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class RateLimiterServiceConfigBuilder {
        private boolean limited;
        private RateLimiterConfig.Builder rateLimiterConfigBuilder;

        public RateLimiterServiceConfig build() {
            return new RateLimiterServiceConfig(this.limited, this.rateLimiterConfigBuilder.build());
        }
    }
}