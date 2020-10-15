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

package io.art.rsocket.model;

import io.art.entity.constants.*;
import io.rsocket.plugins.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.TransportMode.*;
import static io.art.rsocket.module.RsocketModule.*;
import java.util.*;

@Getter
@Accessors(fluent = true)
@Builder(toBuilder = true, builderMethodName = "rsocketCommunicationTarget")
public class RsocketCommunicationTargetConfiguration {
    @Setter
    private String host;
    @Setter
    private Integer tcpPort;
    @Setter
    private Integer webSocketPort;
    @Builder.Default
    private final TransportMode transport = TCP;
    @Builder.Default
    private final EntityConstants.DataFormat dataFormat = rsocketModule().configuration().getDefaultDataFormat();
    @Builder.Default
    private final boolean resumable = rsocketModule().configuration().isResumableClient();
    @Builder.Default
    private final long resumeSessionDuration = rsocketModule().configuration().getClientResumeSessionDuration();
    @Builder.Default
    private final long resumeStreamTimeout = rsocketModule().configuration().getClientResumeStreamTimeout();
    @Singular
    private final List<RSocketInterceptor> interceptors;
}
