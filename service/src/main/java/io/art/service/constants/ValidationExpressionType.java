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

package io.art.service.constants;

public interface ValidationExpressionType {
    String BETWEEN_DOUBLE = "BETWEEN_DOUBLE";
    String BETWEEN_INT = "BETWEEN_INT";
    String BETWEEN_LONG = "BETWEEN_LONG";
    String CONTAINS = "CONTAINS";
    String EQUALS = "EQUALS";
    String NOT_EMPTY_COLLECTION = "NOT_EMPTY_COLLECTION";
    String NOT_EMPTY_MAP = "NOT_EMPTY_MAP";
    String NOT_EMPTY_STRING = "NOT_EMPTY_STRING";
    String NOT_EQUALS = "NOT_EQUALS";
    String NOT_NULL = "NOT_NULL";
}