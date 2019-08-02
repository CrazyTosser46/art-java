package ru.art.http.server.function;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.art.http.server.model.HttpService;
import ru.art.http.server.specification.HttpServiceSpecification;
import static java.util.UUID.randomUUID;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.CharConstants.UNDERSCORE;
import static ru.art.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public class HttpFunctionalServiceSpecification implements HttpServiceSpecification {
    private final String serviceId = HTTP_SERVICE_TYPE + UNDERSCORE + randomUUID();
    private final HttpService httpService;
    private final Function<?, ?> function;

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        return cast(function.apply(cast(request)));
    }
}