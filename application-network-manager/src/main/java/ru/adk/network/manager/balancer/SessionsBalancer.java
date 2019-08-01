package ru.adk.network.manager.balancer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.adk.state.api.model.ModuleEndpoint;
import static java.util.Comparator.comparingInt;
import static java.util.Map.Entry;
import static java.util.stream.Collectors.toMap;
import static ru.adk.core.extension.OptionalExtensions.unwrap;
import static ru.adk.core.factory.CollectionsFactory.concurrentHashMap;
import static ru.adk.core.factory.CollectionsFactory.treeOf;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class SessionsBalancer implements Balancer {
    private final Map<String, Set<ModuleEndpoint>> moduleEndpoints = concurrentHashMap();

    public Collection<ModuleEndpoint> getEndpoints(String modulePath) {
        return moduleEndpoints.get(modulePath);
    }

    @Override
    public void updateEndpoints(Map<String, Collection<ModuleEndpoint>> moduleEndpoints) {
        this.moduleEndpoints.clear();
        this.moduleEndpoints.putAll(moduleEndpoints.entrySet().stream().collect(toMap(Entry::getKey, entry -> treeOf(entry.getValue(), comparingInt(ModuleEndpoint::getSessions)))));
    }

    @Override
    public ModuleEndpoint select(String modulePath) {
        return unwrap(moduleEndpoints.get(modulePath)
                .stream()
                .findFirst()
                .map(response -> ModuleEndpoint.builder().host(response.getHost()).port(response.getPort()).build()));
    }
}
