/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.tarantool.service;

import lombok.experimental.*;
import ru.art.tarantool.configuration.lua.*;
import static java.util.Collections.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.art.core.factory.CollectionsFactory.setOf;
import static ru.art.tarantool.configuration.lua.TarantoolCommonScriptConfiguration.*;
import static ru.art.tarantool.configuration.lua.TarantoolValueScriptConfiguration.*;
import static ru.art.tarantool.executor.TarantoolLuaExecutor.*;
import static ru.art.tarantool.module.TarantoolModule.*;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.*;

@UtilityClass
@SuppressWarnings("Duplicates")
public final class TarantoolScriptService {
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static void evaluateValueScript(String instanceId, String spaceName) {
        TarantoolValueScriptConfiguration valueScriptConfiguration = tarantoolValueScript(spaceName);
        Set<TarantoolValueScriptConfiguration> instanceValueScripts = tarantoolModuleState()
                .getLoadedValueScripts()
                .putIfAbsent(instanceId, emptySet());
        if (nonNull(instanceValueScripts) && instanceValueScripts.contains(valueScriptConfiguration)) {
            return;
        }
        LOCK.lock();
        try {
            if (nonNull(instanceValueScripts) && instanceValueScripts.contains(valueScriptConfiguration)) {
                return;
            }
            evaluateLuaScript(instanceId, valueScriptConfiguration.toLua());
            if (isNull(instanceValueScripts)) {
                tarantoolModuleState().getLoadedValueScripts().put(instanceId, setOf(valueScriptConfiguration));
                return;
            }
            instanceValueScripts.add(valueScriptConfiguration);
        } finally {
            LOCK.unlock();
        }
    }

    public static void evaluateCommonScript(String instanceId, String spaceName) {
        TarantoolCommonScriptConfiguration commonScriptConfiguration = tarantoolCommonScript(spaceName);
        Set<TarantoolCommonScriptConfiguration> instanceCommonScripts = tarantoolModuleState()
                .getLoadedCommonScripts()
                .putIfAbsent(instanceId, emptySet());
        if (nonNull(instanceCommonScripts) && instanceCommonScripts.contains(commonScriptConfiguration)) {
            return;
        }
        LOCK.lock();
        try {
            if (nonNull(instanceCommonScripts) && instanceCommonScripts.contains(commonScriptConfiguration)) {
                return;
            }
            evaluateLuaScript(instanceId, commonScriptConfiguration.toLua());
            if (isNull(instanceCommonScripts)) {
                tarantoolModuleState().getLoadedCommonScripts().put(instanceId, setOf(commonScriptConfiguration));
                return;
            }
            instanceCommonScripts.add(commonScriptConfiguration);
        } finally {
            LOCK.unlock();
        }
    }

    public static void evaluateValueScript(String instanceId, String spaceName, String indexName) {
        TarantoolValueScriptConfiguration valueScriptConfiguration = tarantoolValueScript(spaceName, indexName);
        Set<TarantoolValueScriptConfiguration> instanceValueScripts = tarantoolModuleState()
                .getLoadedValueScripts()
                .putIfAbsent(instanceId, emptySet());
        if (nonNull(instanceValueScripts) && instanceValueScripts.contains(valueScriptConfiguration)) {
            return;
        }
        LOCK.lock();
        try {
            if (nonNull(instanceValueScripts) && instanceValueScripts.contains(valueScriptConfiguration)) {
                return;
            }
            evaluateLuaScript(instanceId, valueScriptConfiguration.toLua());
            if (isNull(instanceValueScripts)) {
                tarantoolModuleState().getLoadedValueScripts().put(instanceId, setOf(valueScriptConfiguration));
                return;
            }
            instanceValueScripts.add(valueScriptConfiguration);
        } finally {
            LOCK.unlock();
        }
    }
}
