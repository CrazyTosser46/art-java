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

package ru.art.tarantool.model;

import lombok.*;
import ru.art.entity.Value;
import ru.art.tarantool.constants.TarantoolModuleConstants.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.entity.Entity.*;
import static ru.art.entity.tuple.PlainTupleWriter.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolOperator.*;
import java.util.*;

@Getter
public class TarantoolUpdateFieldOperation {
    private List<?> valueOperation;
    private List<?> schemaOperation = emptyList();

    private TarantoolUpdateFieldOperation(TarantoolOperator operator, int fieldNumber, int fieldCount) {
        valueOperation = fixedArrayOf(operator.getOperator(), fieldNumber, fieldCount);
        schemaOperation = fixedArrayOf(operator.getOperator(), fieldNumber + 2, fieldCount);
    }

    private TarantoolUpdateFieldOperation(TarantoolOperator operator, int fieldNumber, Object value) {
        valueOperation = fixedArrayOf(operator.getOperator(), fieldNumber, value);
    }

    private TarantoolUpdateFieldOperation(TarantoolOperator operator, int fieldNumber, List<?> value) {
        valueOperation = dynamicArrayOf(operator.getOperator(), fieldNumber);
        valueOperation.addAll(cast(value));
    }

    private TarantoolUpdateFieldOperation(TarantoolOperator operator, int fieldNumber, String fieldName, Value value) {
        PlainTupleWriterResult result = writeTuple(entityBuilder().valueField(fieldName, value).build());
        if (isNull(result)) {
            valueOperation = emptyList();
            return;
        }
        switch (operator) {
            case INSERTION:
            case ASSIGMENT:
                schemaOperation = fixedArrayOf(operator.getOperator(), fieldNumber + 2, result.getSchema().toTuple().get(1));
                break;
        }
        valueOperation = fixedArrayOf(operator.getOperator(), fieldNumber, result.getTuple().get(0));
    }

    public static TarantoolUpdateFieldOperation addition(int fieldNumber, long value) {
        return new TarantoolUpdateFieldOperation(ADDITION, fieldNumber, value);
    }

    public static TarantoolUpdateFieldOperation increment(int fieldNumber) {
        return new TarantoolUpdateFieldOperation(ADDITION, fieldNumber, 1);
    }

    public static TarantoolUpdateFieldOperation subtraction(int fieldNumber, long value) {
        return new TarantoolUpdateFieldOperation(SUBTRACTION, fieldNumber, value);
    }

    public static TarantoolUpdateFieldOperation decrement(int fieldNumber) {
        return new TarantoolUpdateFieldOperation(SUBTRACTION, fieldNumber, 1);
    }

    public static TarantoolUpdateFieldOperation and(int fieldNumber, long value) {
        return new TarantoolUpdateFieldOperation(AND, fieldNumber, value);
    }

    public static TarantoolUpdateFieldOperation or(int fieldNumber, long value) {
        return new TarantoolUpdateFieldOperation(OR, fieldNumber, value);
    }

    public static TarantoolUpdateFieldOperation xor(int fieldNumber, long value) {
        return new TarantoolUpdateFieldOperation(XOR, fieldNumber, value);
    }

    public static TarantoolUpdateFieldOperation stringSplice(int fieldNumber, long fromByte, long byteCount, String valueToInsert) {
        return new TarantoolUpdateFieldOperation(STRING_SPLICE, fieldNumber, fixedArrayOf(fromByte, byteCount, valueToInsert));
    }

    public static TarantoolUpdateFieldOperation insertion(int fieldNumber, String fieldName, Value value) {
        return new TarantoolUpdateFieldOperation(INSERTION, fieldNumber, fieldName, value);
    }

    public static TarantoolUpdateFieldOperation deletion(int fieldNumber, int fieldCount) {
        return new TarantoolUpdateFieldOperation(DELETION, fieldNumber, fieldCount);
    }

    public static TarantoolUpdateFieldOperation assigment(int fieldNumber, String fieldName, Value value) {
        return new TarantoolUpdateFieldOperation(ASSIGMENT, fieldNumber, fieldName, value);
    }
}
