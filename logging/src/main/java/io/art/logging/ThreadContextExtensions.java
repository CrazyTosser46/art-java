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

package io.art.logging;

import io.art.core.checker.*;
import static java.util.Objects.*;
import static org.apache.logging.log4j.ThreadContext.*;

public interface ThreadContextExtensions {
    static void putIfNotNull(String key, Object value) {
        if (isNull(value)) return;
        put(key, value.toString());
    }

    static void putIfNotEmpty(String key, Object value) {
        if (CheckerForEmptiness.isEmpty(value)) return;
        put(key, value.toString());
    }
}