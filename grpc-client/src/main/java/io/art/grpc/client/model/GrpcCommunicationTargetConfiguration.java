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

package io.art.grpc.client.model;

import lombok.*;
import lombok.experimental.*;
import static io.art.grpc.client.module.GrpcClientModule.*;

@Getter
@Accessors(fluent = true)
@Builder(toBuilder = true, builderMethodName = "grpcCommunicationTarget")
public class GrpcCommunicationTargetConfiguration {
    @Builder.Default
    private final String host = grpcClientModule().getBalancerHost();
    @Builder.Default
    private final Integer port = grpcClientModule().getBalancerPort();
    @Builder.Default
    private final long timeout = grpcClientModule().getTimeout();
    @Setter
    private String url;
    @Setter
    private String path;
    @Setter
    private boolean secured;
    @Builder.Default
    private Long keepAliveTimeNanos = grpcClientModule().getKeepAliveTimeNanos();
    @Builder.Default
    private Long keepAliveTimeOutNanos = grpcClientModule().getKeepAliveTimeOutNanos();
    @Builder.Default
    private boolean keepAliveWithoutCalls = grpcClientModule().isKeepAliveWithoutCalls();
    @Builder.Default
    private boolean waitForReady = grpcClientModule().isKeepAliveWithoutCalls();
}