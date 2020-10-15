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

package io.art.rsocket.configuration;

import io.art.rsocket.constants.RsocketModuleConstants.*;
import lombok.*;
import java.time.*;

@Getter
@Builder
public class RsocketResumeConfiguration {
    private final Duration sessionDuration;
    private final Duration streamTimeout;
    private final RetryPolicy retryPolicy;
    private final boolean cleanupStoreOnKeepAlive;
    private final RetryBackoffConfiguration retryBackoffConfiguration;
    private final RetryFixedDelayConfiguration retryFixedDelayConfiguration;
    private final RetryMaxConfiguration retryMaxConfiguration;
    private final RetryMaxInRowConfiguration retryMaxInRowConfiguration;

    @Getter
    @AllArgsConstructor
    public static class RetryBackoffConfiguration {
        private final long maxAttempts;
        private final Duration minBackoff;
    }

    @Getter
    @AllArgsConstructor
    public static class RetryFixedDelayConfiguration {
        private final long maxAttempts;
        private final Duration fixedDelay;
    }

    @Getter
    @AllArgsConstructor
    public static class RetryMaxConfiguration {
        private final int max;
    }

    @Getter
    @AllArgsConstructor
    public static class RetryMaxInRowConfiguration {
        private final int maxInRow;
    }
}