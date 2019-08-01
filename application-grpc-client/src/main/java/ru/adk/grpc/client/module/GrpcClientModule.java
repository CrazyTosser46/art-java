package ru.adk.grpc.client.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleState;
import ru.adk.grpc.client.configuration.GrpcClientModuleConfiguration;
import ru.adk.grpc.client.constants.GrpcClientModuleConstants;
import static ru.adk.core.context.Context.context;
import static ru.adk.grpc.client.configuration.GrpcClientModuleConfiguration.GrpcClientModuleDefaultConfiguration;
import static ru.adk.grpc.client.constants.GrpcClientModuleConstants.GRPC_CLIENT_MODULE_ID;

public class GrpcClientModule implements Module<GrpcClientModuleConfiguration, ModuleState> {
    @Getter
    private final String id = GrpcClientModuleConstants.GRPC_CLIENT_MODULE_ID;
    @Getter
    private final GrpcClientModuleConfiguration defaultConfiguration = new GrpcClientModuleDefaultConfiguration();

    public static GrpcClientModuleConfiguration grpcClientModule() {
        return context().getModule(GRPC_CLIENT_MODULE_ID, new GrpcClientModule());
    }
}
