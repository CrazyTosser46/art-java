/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.entity.tuple.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.art.entity.CollectionValue;
import ru.art.entity.Value;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.entity.constants.ValueType.*;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CollectionValueSchema extends ValueSchema {
    private final CollectionElementsType elementsType;
    private final List<ValueSchema> elementsSchema = dynamicArrayOf();

    CollectionValueSchema(CollectionValue<?> collectionValue, CollectionElementsType elementsType) {
        super(COLLECTION);
        this.elementsType = elementsType;
        switch (collectionValue.getElementsType()) {
            case STRING:
                collectionValue
                        .getElements()
                        .forEach(element -> elementsSchema.add(new ValueSchema(STRING)));
                break;
            case LONG:
                collectionValue
                        .getElements()
                        .forEach(element -> elementsSchema.add(new ValueSchema(LONG)));
                break;
            case DOUBLE:
                collectionValue
                        .getElements()
                        .forEach(element -> elementsSchema.add(new ValueSchema(DOUBLE)));
                break;
            case FLOAT:
                collectionValue
                        .getElements()
                        .forEach(element -> elementsSchema.add(new ValueSchema(FLOAT)));
                break;
            case INT:
                collectionValue
                        .getElements()
                        .forEach(element -> elementsSchema.add(new ValueSchema(INT)));
                break;
            case BOOL:
                collectionValue
                        .getElements()
                        .forEach(element -> elementsSchema.add(new ValueSchema(BOOL)));
                break;
            case BYTE:
                collectionValue
                        .getElements()
                        .forEach(element -> elementsSchema.add(new ValueSchema(BYTE)));
                break;
            case ENTITY:
            case COLLECTION:
            case MAP:
            case STRING_PARAMETERS_MAP:
            case VALUE:
                collectionValue
                        .getElements()
                        .forEach(element -> elementsSchema.add(fromValue((Value) element)));
                break;
        }
    }

    private CollectionValueSchema(CollectionElementsType elementsType) {
        super(COLLECTION);
        this.elementsType = elementsType;
    }

    @Override
    public List<?> toTuple() {
        List<?> tuple = dynamicArrayOf(getType().name(), elementsType.name());
        elementsSchema.stream().map(ValueSchema::toTuple).forEach(value -> tuple.add(cast(value)));
        return tuple;
    }

    public static ValueSchema fromTuple(List<?> tuple) {
        CollectionValueSchema schema = new CollectionValueSchema(CollectionElementsType.valueOf((String) tuple.get(1)));
        tuple.stream()
                .skip(2)
                .map(element -> (List<?>) element)
                .map(ValueSchema::fromTuple)
                .forEach(schema.elementsSchema::add);
        return schema;
    }
}
