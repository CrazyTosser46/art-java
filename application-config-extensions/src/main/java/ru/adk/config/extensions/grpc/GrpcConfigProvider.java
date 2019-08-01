package ru.adk.config.extensions.grpc;

import ru.adk.config.Config;
import static ru.adk.config.extensions.ConfigExtensions.configInner;
import static ru.adk.config.extensions.grpc.GrpcConfigKeys.GRPC_COMMUNICATION_SECTION_ID;
import static ru.adk.config.extensions.grpc.GrpcConfigKeys.GRPC_SERVER_CONFIG_SECTION_ID;

public interface GrpcConfigProvider {
    static Config grpcServerConfig() {
        return configInner(GRPC_SERVER_CONFIG_SECTION_ID);
    }

    static Config grpcCommunicationConfig() {
        return configInner(GRPC_COMMUNICATION_SECTION_ID);
    }
}
