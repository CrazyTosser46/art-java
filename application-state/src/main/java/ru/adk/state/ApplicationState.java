package ru.adk.state;

import lombok.Getter;
import lombok.Setter;
import ru.adk.core.module.ModuleState;
import ru.adk.state.api.model.Cluster;
import ru.adk.state.api.model.ClusterProfile;
import ru.adk.state.api.model.ModuleNetwork;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static ru.adk.core.factory.CollectionsFactory.concurrentHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
public class ApplicationState implements ModuleState {
    private final Map<String, ReentrantLock> clusterLocks = concurrentHashMap();
    private Cluster cluster = Cluster.builder().build();

    public ClusterProfile getClusterProfile(String profile) {
        return cluster.getProfiles().get(profile);
    }

    public Optional<ModuleNetwork> getModuleNetwork(String profile, String modulePath) {
        ClusterProfile clusterProfile;
        if (isNull(clusterProfile = cluster.getProfiles().get(profile))) {
            return empty();
        }
        return ofNullable(clusterProfile.getModuleNetwork(modulePath));
    }
}
