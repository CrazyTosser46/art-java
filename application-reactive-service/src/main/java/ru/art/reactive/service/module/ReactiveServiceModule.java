package ru.art.reactive.service.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.reactive.service.configuration.ReactiveServiceModuleConfiguration;
import ru.art.reactive.service.configuration.ReactiveServiceModuleConfiguration.ReactiveServiceModuleDefaultConfiguration;
import static ru.art.core.context.Context.context;
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.REACTIVE_SERVICE_MODULE_ID;

@Getter
public class ReactiveServiceModule implements Module<ReactiveServiceModuleConfiguration, ModuleState> {
    @Getter(lazy = true)
    private static final ReactiveServiceModuleConfiguration reactiveServiceModule = context().getModule(REACTIVE_SERVICE_MODULE_ID, new ReactiveServiceModule());
    private final ReactiveServiceModuleConfiguration defaultConfiguration = new ReactiveServiceModuleDefaultConfiguration();
    private final String id = REACTIVE_SERVICE_MODULE_ID;

    public static ReactiveServiceModuleConfiguration reactiveServiceModule() {
        return getReactiveServiceModule();
    }
}