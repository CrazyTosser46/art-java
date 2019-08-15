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

package ru.art.service.constants;

public interface ServiceErrorCodes {
    String INTERNAL_ERROR = "SERVICE_INTERNAL_ERROR";
    String CHILD_SERVICE_ERROR = "CHILD_SERVICE_ERROR";
    String UNDECLARED_INTERNAL_ERROR = "SERVICE_UNDECLARED_INTERNAL_ERROR";
    String UNCAUGHT_INTERNAL_ERROR = "SERVICE_UNCAUGHT_INTERNAL_ERROR";
    String NPE = "SERVICE_NULL_POINTER_EXCEPTION";
    String UNKNOWN_METHOD_ERROR = "SERVICE_UNKNOWN_METHOD_ERROR";
}
