package ru.adk.state.api.communication.grpc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import ru.adk.grpc.client.communicator.GrpcCommunicator;
import ru.adk.grpc.client.specification.GrpcCommunicationSpecification;
import ru.adk.service.exception.UnknownServiceMethodException;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.extension.OptionalExtensions.unwrap;
import static ru.adk.entity.PrimitiveMapping.intMapper;
import static ru.adk.grpc.client.communicator.GrpcCommunicator.grpcCommunicator;
import static ru.adk.state.api.constants.StateApiConstants.NetworkServiceConstants.Methods.*;
import static ru.adk.state.api.constants.StateApiConstants.NetworkServiceConstants.NETWORK_COMMUNICATION_SERVICE_ID;
import static ru.adk.state.api.constants.StateApiConstants.NetworkServiceConstants.NETWORK_SERVICE_ID;
import static ru.adk.state.api.mapping.ClusterProfileRequestResponseMapper.ClusterProfileRequestMapper.fromClusterProfileRequest;
import static ru.adk.state.api.mapping.ClusterProfileRequestResponseMapper.ClusterProfileResponseMapper.toClusterProfileResponse;
import static ru.adk.state.api.mapping.ModuleConnectionRequestMapper.fromModuleConnectionRequest;

@Getter
@RequiredArgsConstructor
public class NetworkServiceProxySpecification implements GrpcCommunicationSpecification {
    private final String serviceId = NETWORK_COMMUNICATION_SERVICE_ID;
    private final String path;
    private final String host;
    private final Integer port;

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator getClusterProfile = grpcCommunicator(host, port, path)
            .serviceId(NETWORK_SERVICE_ID)
            .methodId(GET_CLUSTER_PROFILE)
            .requestMapper(fromClusterProfileRequest)
            .responseMapper(toClusterProfileResponse);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator connect = grpcCommunicator(host, port, path)
            .serviceId(NETWORK_SERVICE_ID)
            .requestMapper(fromModuleConnectionRequest)
            .methodId(CONNECT);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator incrementSession = grpcCommunicator(host, port, path)
            .serviceId(NETWORK_SERVICE_ID)
            .methodId(INCREMENT_SESSION)
            .requestMapper(fromModuleConnectionRequest);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator decrementSession = grpcCommunicator(host, port, path)
            .serviceId(NETWORK_SERVICE_ID)
            .methodId(DECREMENT_SESSION)
            .requestMapper(fromModuleConnectionRequest);

    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final GrpcCommunicator getSessions = grpcCommunicator(host, port, path)
            .serviceId(NETWORK_SERVICE_ID)
            .methodId(GET_SESSIONS)
            .requestMapper(fromModuleConnectionRequest)
            .responseMapper(intMapper.getToModel());

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case GET_CLUSTER_PROFILE:
                return cast(unwrap(getClusterProfile().execute(cast(request))));
            case CONNECT:
                connect().asynchronous().executeAsynchronous(cast(request));
                return null;
            case INCREMENT_SESSION:
                incrementSession().asynchronous().executeAsynchronous(cast(request));
                return null;
            case DECREMENT_SESSION:
                decrementSession().asynchronous().executeAsynchronous(cast(request));
                return null;
            case GET_SESSIONS:
                return cast(unwrap(getSessions().execute(cast(request))));
        }
        throw new UnknownServiceMethodException(getServiceId(), methodId);
    }
}
