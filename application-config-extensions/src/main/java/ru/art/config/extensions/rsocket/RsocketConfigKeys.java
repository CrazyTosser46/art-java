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

package ru.art.config.extensions.rsocket;

public interface RsocketConfigKeys {
    String RSOCKET_SECTION_ID = "rsocket";
    String RSOCKET_BALANCER_SECTION_ID = "rsocket.balancer";
    String RSOCKET_SERVER_SECTION_ID = "rsocket.server";
    String RSOCKET_COMMUNICATION_SECTION_ID = "rsocket.communication";
    String TCP_PORT = "tcpPort";
    String WEB_SOCKET_PORT = "webSocketPort";
    String DATA_FORMAT = "dataFormat";
    String RESUMABLE = "resumable";
    String RESUME_SESSION_DURATION = "resumeSessionDuration";
    String RESUME_STREAM_TIMEOUT = "resumeStreamTimeout";
    String FRAGMENTATION_MTU = "fragmentationMtu";
}
