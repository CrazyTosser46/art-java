package ru.adk.tarantool.service;

import lombok.NoArgsConstructor;
import org.tarantool.TarantoolClient;
import ru.adk.tarantool.configuration.lua.TarantoolSpaceConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.tarantool.caller.TarantoolFunctionCaller.callTarantoolFunction;
import static ru.adk.tarantool.configuration.lua.TarantoolSpaceConfiguration.*;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Functions.DROP;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Functions.RENAME;
import static ru.adk.tarantool.executor.TarantoolLuaExecutor.evaluateLuaScript;
import static ru.adk.tarantool.module.TarantoolModule.tarantoolModuleState;
import java.util.Set;

@NoArgsConstructor(access = PRIVATE)
public final class TarantoolSpaceService {
    public static void createSpace(String instanceId, String name) {
        evaluateLuaScript(instanceId, tarantoolSpace(name).toCreateSpaceLua());
        evaluateLuaScript(instanceId, tarantoolSpace(name).toManageSpaceLua());
    }

    public static void createSpace(String instanceId, TarantoolSpaceConfiguration configuration) {
        evaluateLuaScript(instanceId, configuration.toCreateSpaceLua());
        evaluateLuaScript(instanceId, configuration.toManageSpaceLua());
    }

    public static void dropSpace(String instanceId, String spaceName) {
        evaluateLuaScript(instanceId, tarantoolSpace(spaceName).toManageSpaceLua());
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        callTarantoolFunction(client, DROP + spaceName);
    }

    public static void formatSpace(String instanceId, String spaceName, Set<TarantoolSpaceConfiguration.Format> fieldsFormat) {
        evaluateLuaScript(instanceId, TarantoolSpaceConfiguration.builder().spaceName(spaceName).formats(fieldsFormat).build().toFormatSpaceLua());
    }

    public static void renameSpace(String instanceId, String currentSpaceName, String newSpaceName) {
        evaluateLuaScript(instanceId, tarantoolSpace(currentSpaceName).toManageSpaceLua());
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        callTarantoolFunction(client, RENAME + currentSpaceName, fixedArrayOf(newSpaceName));
    }
}


