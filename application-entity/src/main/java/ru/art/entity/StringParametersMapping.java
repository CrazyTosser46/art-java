package ru.art.entity;

import ru.art.entity.mapper.ValueFromModelMapper.StringParametersMapFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper.StringParametersMapToModelMapper;
import static ru.art.core.extension.StringExtensions.emptyIfNull;
import static ru.art.entity.mapper.ValueMapper.mapper;

public interface StringParametersMapping {
    static ValueMapper<String, StringParametersMap> stringParamToStringMapper(String key) {
        return mapper(StringParameterMapping.fromModel(key), StringParameterMapping.toModel(key));
    }

    static ValueMapper<Integer, StringParametersMap> stringParamToIntMapper(String key) {
        return mapper(IntParameterMapping.fromModel(key), IntParameterMapping.toModel(key));
    }

    static ValueMapper<Long, StringParametersMap> stringParamToLongMapper(String key) {
        return mapper(LongParameterMapping.fromModel(key), LongParameterMapping.toModel(key));
    }

    static ValueMapper<Double, StringParametersMap> stringParamToDoubleMapper(String key) {
        return mapper(DoubleParameterMapping.fromModel(key), DoubleParameterMapping.toModel(key));
    }

    static ValueMapper<Boolean, StringParametersMap> stringParamToBoolMapper(String key) {
        return mapper(BooleanParameterMapping.fromModel(key), BooleanParameterMapping.toModel(key));
    }

    static ValueMapper<Byte, StringParametersMap> stringParamToByteMapper(String key) {
        return mapper(ByteParameterMapping.fromModel(key), ByteParameterMapping.toModel(key));
    }

    static ValueMapper<Float, StringParametersMap> stringParamToFloatMapper(String key) {
        return mapper(FloatParameterMapping.fromModel(key), FloatParameterMapping.toModel(key));
    }

    interface StringParameterMapping {
        static StringParametersMapFromModelMapper<String> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, str).build();
        }

        static StringParametersMapToModelMapper<String> toModel(String name) {
            return keyValue -> keyValue.getParameters().get(name);
        }
    }

    interface IntParameterMapping {
        static StringParametersMapFromModelMapper<Integer> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, emptyIfNull(str)).build();
        }

        static StringParametersMapToModelMapper<Integer> toModel(String name) {
            return keyValue -> Integer.valueOf(keyValue.getParameters().get(name));
        }
    }

    interface LongParameterMapping {
        static StringParametersMapFromModelMapper<Long> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, emptyIfNull(str)).build();
        }

        static StringParametersMapToModelMapper<Long> toModel(String name) {
            return keyValue -> Long.valueOf(keyValue.getParameters().get(name));
        }
    }

    interface DoubleParameterMapping {
        static StringParametersMapFromModelMapper<Double> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, emptyIfNull(str)).build();
        }

        static StringParametersMapToModelMapper<Double> toModel(String name) {
            return keyValue -> Double.valueOf(keyValue.getParameters().get(name));
        }
    }

    interface BooleanParameterMapping {
        static StringParametersMapFromModelMapper<Boolean> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, emptyIfNull(str)).build();
        }

        static StringParametersMapToModelMapper<Boolean> toModel(String name) {
            return keyValue -> Boolean.valueOf(keyValue.getParameters().get(name));
        }
    }

    interface ByteParameterMapping {
        static StringParametersMapFromModelMapper<Byte> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, emptyIfNull(str)).build();
        }

        static StringParametersMapToModelMapper<Byte> toModel(String name) {
            return keyValue -> Byte.valueOf(keyValue.getParameters().get(name));
        }
    }

    interface FloatParameterMapping {
        static StringParametersMapFromModelMapper<Float> fromModel(String name) {
            return str -> StringParametersMap.builder().parameter(name, emptyIfNull(str)).build();
        }

        static StringParametersMapToModelMapper<Float> toModel(String name) {
            return keyValue -> Float.valueOf(keyValue.getParameters().get(name));
        }
    }
}