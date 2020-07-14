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

package io.art.config.extensions.rsocket;

import lombok.experimental.*;
import io.art.config.*;
import static io.art.config.extensions.ConfigExtensions.*;
import static io.art.config.extensions.rsocket.RsocketConfigKeys.*;

@UtilityClass
public class RsocketConfigProvider {
    public static Config rsocketConfig() {
        return configInner(RSOCKET_SECTION_ID);
    }
}