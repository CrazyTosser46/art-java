package ru.art.network.manager.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.art.network.manager.balancer.Balancer;
import ru.art.state.api.model.ModuleNetworkResponse;
import static java.util.stream.Collectors.toMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ClusterState {
    private final Balancer balancer;

    public void updateModuleEndpoints(Map<String, ModuleNetworkResponse> moduleEndpoints) {
        this.balancer.updateEndpoints(moduleEndpoints
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().getEndpoints())));
    }

}