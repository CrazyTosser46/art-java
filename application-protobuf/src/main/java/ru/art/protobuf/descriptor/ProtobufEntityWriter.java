package ru.art.protobuf.descriptor;

import com.google.protobuf.*;
import lombok.NoArgsConstructor;
import ru.art.entity.Value;
import ru.art.entity.*;
import ru.art.entity.constants.ValueType.CollectionElementsType;
import ru.art.protobuf.entity.ProtobufValueMessage.ProtobufCollection;
import ru.art.protobuf.entity.ProtobufValueMessage.ProtobufEntity;
import ru.art.protobuf.entity.ProtobufValueMessage.ProtobufStringParametersMap;
import ru.art.protobuf.entity.ProtobufValueMessage.ProtobufValue;
import ru.art.protobuf.exception.ProtobufException;
import static com.google.protobuf.Any.pack;
import static com.google.protobuf.ByteString.copyFrom;
import static java.text.MessageFormat.format;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.extension.FileExtensions.writeFileQuietly;
import static ru.art.entity.Value.*;
import static ru.art.entity.constants.ValueType.CollectionElementsType.BYTE;
import static ru.art.protobuf.constants.ProtobufExceptionMessages.VALUE_TYPE_NOT_SUPPORTED;
import static ru.art.protobuf.entity.ProtobufValueMessage.ProtobufValue.Type.BYTE_STRING;
import static ru.art.protobuf.entity.ProtobufValueMessage.ProtobufValue.getDefaultInstance;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = PRIVATE)
public class ProtobufEntityWriter {
    public static ProtobufValue writeProtobuf(Value value) {
        if (isEmpty(value)) return getDefaultInstance();
        switch (value.getType()) {
            case STRING:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.STRING)
                        .setValue(pack(StringValue.newBuilder().setValue(asPrimitive(value).getString()).build()))
                        .build();
            case LONG:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.LONG)
                        .setValue(pack(Int64Value.newBuilder().setValue(asPrimitive(value).getLong()).build()))
                        .build();
            case DOUBLE:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.DOUBLE)
                        .setValue(pack(DoubleValue.newBuilder().setValue(asPrimitive(value).getDouble()).build()))
                        .build();
            case FLOAT:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.FLOAT)
                        .setValue(pack(FloatValue.newBuilder().setValue(asPrimitive(value).getFloat()).build()))
                        .build();
            case INT:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.INT)
                        .setValue(pack(Int32Value.newBuilder().setValue(asPrimitive(value).getInt()).build()))
                        .build();
            case BYTE:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.BYTE)
                        .setValue(pack(Int32Value.newBuilder().setValue(asPrimitive(value).getByte()).build()))
                        .build();
            case BOOL:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.BOOL)
                        .setValue(pack(BoolValue.newBuilder().setValue(asPrimitive(value).getBool()).build()))
                        .build();
            case ENTITY:
                return writeEntityToProtobuf(asEntity(value));
            case COLLECTION:
                return writeCollectionToProtobuf(asCollection(value));
            case MAP:
                return writeMapToProtobuf(asMap(value));
            case STRING_PARAMETERS_MAP:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.STRING_PARAMETERS_MAP)
                        .setValue(pack(ProtobufStringParametersMap.newBuilder().putAllParameters(asStringParametersMap(value).getParameters()).build()))
                        .build();
        }
        throw new ProtobufException(format(VALUE_TYPE_NOT_SUPPORTED, value.getType()));
    }

    public static void writeProtobuf(Value value, Path path) {
        writeFileQuietly(path, writeProtobuf(value).toByteArray());
    }

    private static ProtobufValue writeMapToProtobuf(MapValue map) {
        Map<String, ProtobufValue> protobufValueMap = map.getElements()
                .entrySet()
                .stream()
                .filter(entry -> nonNull(entry.getValue()))
                .filter(entry -> isPrimitive(entry.getKey()))
                .filter(entry -> !entry.getKey().isEmpty())
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(toMap(entry -> asPrimitive(entry.getKey()).toString(), entry -> writeProtobuf(entry.getValue())));
        ProtobufEntity protobufMap = ProtobufEntity.newBuilder()
                .putAllValues(protobufValueMap)
                .build();
        return ProtobufValue.newBuilder()
                .setValueType(ProtobufValue.Type.MAP)
                .setValue(pack(protobufMap))
                .build();
    }

    private static ProtobufValue writeCollectionToProtobuf(CollectionValue<?> collectionValue) {
        if (BYTE == collectionValue.getElementsType()) {
            return ProtobufValue.newBuilder()
                    .setValueType(BYTE_STRING)
                    .setValue(Any.newBuilder().setValue(copyFrom(collectionValue.getByteArray())).build())
                    .build();
        }
        List<ProtobufValue> protobufValues = collectionValue.getElements()
                .stream()
                .map(element -> writeCollectionValueToProtobuf(collectionValue.getElementsType(), element))
                .collect(toList());
        ProtobufCollection.Builder builder = ProtobufCollection.newBuilder();
        switch (collectionValue.getElementsType()) {
            case VALUE:
                builder.setElementsType(ProtobufValue.Type.VALUE);
            case ENTITY:
                builder.setElementsType(ProtobufValue.Type.ENTITY);
                break;
            case COLLECTION:
                builder.setElementsType(ProtobufValue.Type.COLLECTION);
                break;
            case STRING_PARAMETERS_MAP:
                builder.setElementsType(ProtobufValue.Type.STRING_PARAMETERS_MAP);
                break;
            case STRING:
                builder.setElementsType(ProtobufValue.Type.STRING);
                break;
            case LONG:
                builder.setElementsType(ProtobufValue.Type.LONG);
                break;
            case DOUBLE:
                builder.setElementsType(ProtobufValue.Type.DOUBLE);
                break;
            case FLOAT:
                builder.setElementsType(ProtobufValue.Type.FLOAT);
                break;
            case INT:
                builder.setElementsType(ProtobufValue.Type.INT);
                break;
            case BOOL:
                builder.setElementsType(ProtobufValue.Type.BOOL);
                break;
            default:
                throw new ProtobufException(format(VALUE_TYPE_NOT_SUPPORTED, collectionValue.getElementsType()));

        }
        ProtobufCollection protobufCollection = builder
                .addAllValues(protobufValues)
                .build();
        return ProtobufValue.newBuilder()
                .setValueType(ProtobufValue.Type.COLLECTION)
                .setValue(pack(protobufCollection))
                .build();
    }

    private static <T> ProtobufValue writeCollectionValueToProtobuf(CollectionElementsType type, T value) {
        switch (type) {
            case STRING:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.STRING)
                        .setValue(pack(StringValue.newBuilder().setValue((String) value).build()))
                        .build();
            case LONG:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.LONG)
                        .setValue(pack(Int64Value.newBuilder().setValue((Long) value).build()))
                        .build();
            case DOUBLE:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.DOUBLE)
                        .setValue(pack(DoubleValue.newBuilder().setValue((Double) value).build()))
                        .build();
            case FLOAT:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.FLOAT)
                        .setValue(pack(FloatValue.newBuilder().setValue((Float) value).build()))
                        .build();
            case INT:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.INT)
                        .setValue(pack(Int32Value.newBuilder().setValue((Integer) value).build()))
                        .build();
            case BOOL:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.BOOL)
                        .setValue(pack(BoolValue.newBuilder().setValue((Boolean) value).build()))
                        .build();
            case ENTITY:
                return writeEntityToProtobuf((Entity) value);
            case COLLECTION:
                return writeCollectionToProtobuf((CollectionValue<?>) value);
            case STRING_PARAMETERS_MAP:
                return ProtobufValue.newBuilder()
                        .setValueType(ProtobufValue.Type.STRING_PARAMETERS_MAP)
                        .setValue(pack(ProtobufStringParametersMap.newBuilder().putAllParameters(((StringParametersMap) value).getParameters()).build()))
                        .build();
            case VALUE:
                return writeProtobuf((Value) value);
        }
        throw new ProtobufException(format(VALUE_TYPE_NOT_SUPPORTED, type));
    }

    private static ProtobufValue writeEntityToProtobuf(Entity entity) {
        Map<String, ProtobufValue> protobufValueMap = entity.getFields().entrySet()
                .stream()
                .filter(entry -> nonNull(entry.getValue()))
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(toMap(Map.Entry::getKey, entry -> writeProtobuf(entry.getValue())));
        ProtobufEntity protobufEntity = ProtobufEntity.newBuilder()
                .putAllValues(protobufValueMap)
                .build();
        return ProtobufValue.newBuilder()
                .setValueType(ProtobufValue.Type.ENTITY)
                .setValue(pack(protobufEntity))
                .build();
    }
}