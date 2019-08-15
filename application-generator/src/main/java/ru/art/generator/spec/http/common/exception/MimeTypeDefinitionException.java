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

package ru.art.generator.spec.http.common.exception;

/**
 * Thrown to indicate that some error during mime type's
 * identification occurred.
 */
public class MimeTypeDefinitionException extends RuntimeException {
    /**
     * Constructs a <code>MimeTypeDefinitionException</code> with the
     * specified detail message.
     *
     * @param message - message's details.
     */
    public MimeTypeDefinitionException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>MimeTypeDefinitionException</code> with
     * the specified detail message and cause.
     *
     * @param message - message's details.
     * @param cause
     */
    public MimeTypeDefinitionException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>MimeTypeDefinitionException</code> with
     * the specified cause.
     *
     * @param cause
     */
    public MimeTypeDefinitionException(Exception cause) {
        super(cause);
    }
}
