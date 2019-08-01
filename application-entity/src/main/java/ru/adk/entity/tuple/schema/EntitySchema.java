package ru.adk.entity.tuple.schema;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.adk.entity.Entity;
import ru.adk.entity.Value;
import ru.adk.entity.constants.ValueType;
import ru.adk.entity.exception.ValueMappingException;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.adk.entity.Value.isPrimitiveType;
import static ru.adk.entity.constants.ValueMappingExceptionMessages.VALUE_TYPE_IS_NULL;
import static ru.adk.entity.constants.ValueType.ENTITY;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class EntitySchema extends ValueSchema {
    private final List<EntityFieldSchema> fieldsSchema = dynamicArrayOf();

    EntitySchema(Entity entity) {
        super(ENTITY);
        Set<? extends Map.Entry<String, ? extends Value>> fields = entity.getFields().entrySet();
        for (Map.Entry<String, ? extends Value> entry : fields) {
            fieldsSchema.add(new EntityFieldSchema(entry.getValue().getType(), entry.getKey(), fromValue(entry.getValue())));
        }
    }

    private EntitySchema() {
        super(ENTITY);
    }

    @Override
    public List<?> toTuple() {
        List<?> tuple = dynamicArrayOf(getType().name());
        fieldsSchema.stream().map(EntityFieldSchema::toTuple).forEach(value -> tuple.add(cast(value)));
        return tuple;
    }

    public static EntitySchema fromTuple(List<?> tuple) {
        EntitySchema schema = new EntitySchema();
        tuple.stream()
                .skip(1)
                .map(element -> (List<?>) element)
                .map(EntityFieldSchema::fromTuple)
                .forEach(schema.fieldsSchema::add);
        return schema;
    }

    @Getter
    @EqualsAndHashCode
    public static class EntityFieldSchema {
        private final String name;
        private final ValueType type;
        private final ValueSchema schema;

        EntityFieldSchema(ValueType type, String name, ValueSchema schema) {
            this.type = type;
            this.name = name;
            this.schema = schema;
        }

        public List<?> toTuple() {
            if (isNull(schema)) {
                return dynamicArrayOf(type.name(), name, emptyList());
            }
            return dynamicArrayOf(type.name(), name, schema.toTuple());
        }

        public static EntityFieldSchema fromTuple(List<?> tuple) {
            ValueType type = ValueType.valueOf((String) tuple.get(0));
            String name = (String) tuple.get(1);
            if (isPrimitiveType(type)) {
                return new EntityFieldSchema(type, name, new ValueSchema(type));
            }

            switch (type) {
                case ENTITY:
                    return new EntityFieldSchema(type, name, EntitySchema.fromTuple((List<?>) tuple.get(2)));
                case COLLECTION:
                    return new EntityFieldSchema(type, name, CollectionValueSchema.fromTuple((List<?>) tuple.get(2)));
                case MAP:
                    return new EntityFieldSchema(type, name, MapValueSchema.fromTuple((List<?>) tuple.get(2)));
                case STRING_PARAMETERS_MAP:
                    return new EntityFieldSchema(type, name, StringParametersSchema.fromTuple((List<?>) tuple.get(2)));
            }
            throw new ValueMappingException(VALUE_TYPE_IS_NULL);
        }
    }
}
