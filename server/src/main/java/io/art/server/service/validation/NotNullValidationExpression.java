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

package io.art.server.service.validation;

import static io.art.server.constants.ServiceExceptionsMessages.*;
import static io.art.server.constants.ValidationExpressionType.*;

class NotNullValidationExpression extends ValidationExpression<Object> {

    NotNullValidationExpression() {
        super(NOT_NULL);
    }

    NotNullValidationExpression(String pattern) {
        super(NOT_NULL);
        this.pattern = pattern;
    }

    @Override
    public boolean evaluate(String fieldName, Object value) {
        return super.evaluate(fieldName, value);
    }

    @Override
    public String getValidationErrorMessage() {
        return format(NULL_VALIDATION_ERROR, fieldName);
    }
}