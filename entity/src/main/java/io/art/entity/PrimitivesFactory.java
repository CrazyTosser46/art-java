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

package io.art.entity;

import io.art.entity.constants.*;

public interface PrimitivesFactory {
    static Primitive stringPrimitive(String value) {
        return new Primitive(value, ValueType.PrimitiveType.STRING);
    }

    static Primitive longPrimitive(Long value) {
        return new Primitive(value, ValueType.PrimitiveType.LONG);
    }

    static Primitive intPrimitive(Integer value) {
        return new Primitive(value, ValueType.PrimitiveType.INT);
    }

    static Primitive boolPrimitive(Boolean value) {
        return new Primitive(value, ValueType.PrimitiveType.BOOL);
    }

    static Primitive doublePrimitive(Double value) {
        return new Primitive(value, ValueType.PrimitiveType.DOUBLE);
    }

    static Primitive bytePrimitive(Byte value) {
        return new Primitive(value, ValueType.PrimitiveType.BYTE);
    }

    static Primitive floatPrimitive(Float value) {
        return new Primitive(value, ValueType.PrimitiveType.FLOAT);
    }

    static Primitive longPrimitive(long value) {
        return new Primitive(value, ValueType.PrimitiveType.LONG);
    }

    static Primitive intPrimitive(int value) {
        return new Primitive(value, ValueType.PrimitiveType.INT);
    }

    static Primitive boolPrimitive(boolean value) {
        return new Primitive(value, ValueType.PrimitiveType.BOOL);
    }

    static Primitive doublePrimitive(double value) {
        return new Primitive(value, ValueType.PrimitiveType.DOUBLE);
    }

    static Primitive bytePrimitive(byte value) {
        return new Primitive(value, ValueType.PrimitiveType.BYTE);
    }

    static Primitive floatPrimitive(float value) {
        return new Primitive(value, ValueType.PrimitiveType.FLOAT);
    }
}
