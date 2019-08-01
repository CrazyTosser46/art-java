package ru.adk.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import ru.adk.core.checker.CheckerForEmptiness;
import ru.adk.entity.constants.ValueType;
import static ru.adk.entity.constants.ValueType.STRING_PARAMETERS_MAP;
import java.util.Map;


@Getter
@Builder
@EqualsAndHashCode
public class StringParametersMap implements Value {
    private final ValueType type = STRING_PARAMETERS_MAP;
    @Singular
    private Map<String, String> parameters;

    public String getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public String toString() {
        return parameters.toString();
    }

    @Override
    public boolean isEmpty() {
        return CheckerForEmptiness.isEmpty(parameters);
    }
}
