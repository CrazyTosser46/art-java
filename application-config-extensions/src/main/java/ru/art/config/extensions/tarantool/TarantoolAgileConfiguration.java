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

package ru.art.config.extensions.tarantool;

import lombok.*;
import ru.art.config.*;
import ru.art.tarantool.configuration.*;
import ru.art.tarantool.configuration.TarantoolModuleConfiguration.*;
import ru.art.tarantool.configuration.lua.*;
import java.util.*;
import java.util.function.*;

import static java.util.Collections.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.*;
import static ru.art.config.extensions.tarantool.TarantoolConfigKeys.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.constants.NetworkConstants.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.ExceptionExtensions.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolInstanceMode.*;
import static ru.art.tarantool.model.TarantoolEntityFieldsMapping.*;

@Getter
public class TarantoolAgileConfiguration extends TarantoolModuleDefaultConfiguration {
    private boolean enableTracing;
    private long probeConnectionTimeout;
    private long connectionTimeout;
    private TarantoolInitializationMode initializationMode;
    private TarantoolLocalConfiguration localConfiguration;
    private Map<String, TarantoolConfiguration> tarantoolConfigurations;

    public TarantoolAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        enableTracing = configBoolean(TARANTOOL_SECTION_ID, ENABLE_TRACING, super.isEnableTracing());
        probeConnectionTimeout = configLong(TARANTOOL_SECTION_ID, PROBE_CONNECTION_TIMEOUT, super.getProbeConnectionTimeout());
        connectionTimeout = configLong(TARANTOOL_SECTION_ID, CONNECTION_TIMEOUT, super.getConnectionTimeout());
        initializationMode = super.getInitializationMode();
        initializationMode = ifException(() -> TarantoolInitializationMode.valueOf(configString(TARANTOOL_SECTION_ID, INITIALIZATION_MODE).toUpperCase()), initializationMode);
        TarantoolLocalConfiguration defaultLocalConfiguration = super.getLocalConfiguration();
        List<String> executableApplicationName = defaultLocalConfiguration.getExecutableCommand();
        executableApplicationName = configStringList(TARANTOOL_LOCAL_SECTION_ID, EXECUTABLE_COMMAND, executableApplicationName);
        String workingDirectory = defaultLocalConfiguration.getWorkingDirectory();
        workingDirectory = configString(TARANTOOL_LOCAL_SECTION_ID, WORKING_DIRECTORY, workingDirectory);
        localConfiguration = TarantoolLocalConfiguration.builder()
                .executableCommand(executableApplicationName)
                .workingDirectory(workingDirectory)
                .build();
        Function<Config, TarantoolConfiguration> mapper = config -> TarantoolConfiguration.builder()
                .connectionConfiguration(TarantoolConnectionConfiguration.builder()
                        .host(ifExceptionOrEmpty(() -> config.getString(CONNECTION_SECTION_ID + DOT + HOST), LOCALHOST))
                        .port(ifExceptionOrEmpty(() -> config.getInt(CONNECTION_SECTION_ID + DOT + PORT), DEFAULT_TARANTOOL_PORT))
                        .username(ifExceptionOrEmpty(() -> config.getString(CONNECTION_SECTION_ID + DOT + USERNAME), DEFAULT_TARANTOOL_USERNAME))
                        .password(ifExceptionOrEmpty(() -> config.getString(CONNECTION_SECTION_ID + DOT + PASSWORD), EMPTY_STRING))
                        .build())
                .initialConfiguration(TarantoolInitialConfiguration.builder()
                        .background(nullIfException(() -> config.getBool(INITIAL_SECTION_ID + DOT + BACKGROUND)))
                        .customProcTitle(nullIfException(() -> config.getString(INITIAL_SECTION_ID + DOT + CUSTOM_PROC_TITLE)))
                        .memtxDir(nullIfException(() -> config.getString(INITIAL_SECTION_ID + DOT + MEMTX_DIR)))
                        .vinylDir(nullIfException(() -> config.getString(INITIAL_SECTION_ID + DOT + VINYL_DIR)))
                        .workDir(nullIfException(() -> config.getString(INITIAL_SECTION_ID + DOT + WORK_DIR)))
                        .pidFile(nullIfException(() -> config.getString(INITIAL_SECTION_ID + DOT + PID_FILE)))
                        .readOnly(nullIfException(() -> config.getBool(INITIAL_SECTION_ID + DOT + READ_ONLY)))
                        .vinylTimeout(nullIfException(() -> config.getLong(INITIAL_SECTION_ID + DOT + VINYL_TIMEOUT)))
                        .workerPoolThreads(nullIfException(() -> config.getInt(INITIAL_SECTION_ID + DOT + WORKER_POOL_THREADS)))
                        .memtexMaxTupleSize(nullIfException(() -> config.getLong(INITIAL_SECTION_ID + DOT + MEMTEX_MAX_TUPLE_SIZE)))
                        .memtxMemory(nullIfException(() -> config.getLong(INITIAL_SECTION_ID + DOT + MEMTX_MEMORY)))
                        .slabAllocFactor(nullIfException(() -> config.getInt(INITIAL_SECTION_ID + DOT + SLAB_ALLOC_FACTOR))).build())
                .instanceMode(ifException(() -> TarantoolInstanceMode.valueOf(config.getString(INSTANCE_MODE).toUpperCase()), LOCAL))
                .entityFieldsMappings(ifExceptionOrEmpty(() -> config.getConfig(ENTITIES).getKeys()
                        .stream().collect(toMap(identity(), entityName -> entityFieldsMapping()
                                .fieldsMapping(cast(config.getConfig(ENTITIES + DOT + entityName + DOT + FIELDS)
                                        .getKeys()
                                        .stream()
                                        .collect(toMap(identity(), (String fieldName) ->
                                                config.getInt(ENTITIES + DOT + entityName + DOT + FIELDS + DOT + fieldName)))))
                                .map())), emptyMap()))
                .build();
        tarantoolConfigurations = configMap(TARANTOOL_CONFIGURATIONS_SECTION_ID, mapper, super.getTarantoolConfigurations());
    }
}
