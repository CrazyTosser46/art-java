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

package io.art.value.mapper;

import io.art.value.exception.*;
import io.art.value.immutable.*;
import static io.art.value.constants.ValueConstants.ExceptionMessages.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.Objects.isNull;
import java.io.*;
import java.util.*;

public interface ValueToModelMapper<T, V extends Value> extends Serializable {
    T map(V value);

    interface EntityToModelMapper<T> extends ValueToModelMapper<T, Entity> {
    }

    interface ArrayToModelMapper<T> extends ValueToModelMapper<T, ArrayValue> {
    }

    interface PrimitiveToModelMapper<T> extends ValueToModelMapper<T, Primitive> {

    }

    interface BinaryToModelMapper<T> extends ValueToModelMapper<T, BinaryValue> {
    }

    interface XmlToModelMapper<T> extends ValueToModelMapper<T, XmlEntity> {
    }

    static <V extends Value> ValueToModelMapper<V, V> identity() {
        return value -> value;
    }

    static <T> PrimitiveToModelMapper<T> validatePrimitiveMapping(String name, PrimitiveToModelMapper<T> delegate) {
        return (PrimitiveToModelMapper<T>) value -> {
            if (isNull(value)) {
                System.err.println(format(NULL_FIELD_MESSAGE, name));
                throw new ValueMappingException(format(NULL_FIELD_MESSAGE, name));
            }
            T result;
            if (isNull(result = delegate.map(value))) {
                System.err.println(format(NULL_FIELD_MESSAGE, name));
                throw new ValueMappingException(format(NULL_FIELD_MESSAGE, name));
            }
            return result;
        };
    }
}
