package ru.adk.config.extensions.tarantool;

import lombok.Getter;
import ru.adk.config.Config;
import ru.adk.tarantool.configuration.TarantoolConfiguration;
import ru.adk.tarantool.configuration.TarantoolConnectionConfiguration;
import ru.adk.tarantool.configuration.TarantoolLocalConfiguration;
import ru.adk.tarantool.configuration.TarantoolModuleConfiguration.TarantoolModuleDefaultConfiguration;
import ru.adk.tarantool.configuration.lua.TarantoolInitialConfiguration;
import static ru.adk.config.extensions.ConfigExtensions.*;
import static ru.adk.config.extensions.common.CommonConfigKeys.*;
import static ru.adk.config.extensions.tarantool.TarantoolConfigKeys.*;
import static ru.adk.core.constants.NetworkConstants.LOCALHOST;
import static ru.adk.core.constants.StringConstants.DOT;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.core.extension.ExceptionExtensions.ifException;
import static ru.adk.core.extension.ExceptionExtensions.nullIfException;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.*;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.TarantoolInstanceMode.LOCAL;
import java.util.Map;
import java.util.function.Function;

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
        String executableApplicationName = defaultLocalConfiguration.getExecutableApplicationName();
        executableApplicationName = configString(TARANTOOL_LOCAL_SECTION_ID, EXECUTABLE_APPLICATION_NAME, executableApplicationName);
        Long startupTimeoutSeconds = defaultLocalConfiguration.getStartupTimeoutSeconds();
        startupTimeoutSeconds = configLong(TARANTOOL_LOCAL_SECTION_ID, STARTUP_TIMEOUT_SECONDS, startupTimeoutSeconds);
        String workingDirectory = defaultLocalConfiguration.getWorkingDirectory();
        workingDirectory = configString(TARANTOOL_LOCAL_SECTION_ID, WORKING_DIRECTORY, workingDirectory);
        localConfiguration = TarantoolLocalConfiguration.builder()
                .executableApplicationName(executableApplicationName)
                .startupTimeoutSeconds(startupTimeoutSeconds)
                .workingDirectory(workingDirectory)
                .build();
        Function<Config, TarantoolConfiguration> mapper = config -> TarantoolConfiguration.builder()
                .connectionConfiguration(TarantoolConnectionConfiguration.builder()
                        .host(ifException(() -> config.getString(CONNECTION_SECTION_ID + DOT + HOST), LOCALHOST))
                        .port(ifException(() -> config.getInt(CONNECTION_SECTION_ID + DOT + PORT), DEFAULT_TARANTOOL_PORT))
                        .username(ifException(() -> config.getString(CONNECTION_SECTION_ID + DOT + USERNAME), DEFAULT_TARANTOOL_USERNAME))
                        .password(ifException(() -> config.getString(CONNECTION_SECTION_ID + DOT + PASSWORD), EMPTY_STRING))
                        .build())
                .initialConfiguration(TarantoolInitialConfiguration.builder()
                        .port(DEFAULT_TARANTOOL_PORT)
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
                .build();
        tarantoolConfigurations = configMap(TARANTOOL_CONFIGURATIONS_SECTION_ID, mapper, super.getTarantoolConfigurations());
    }
}
