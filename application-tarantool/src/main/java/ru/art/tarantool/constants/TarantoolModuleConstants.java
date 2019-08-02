package ru.art.tarantool.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import static ru.art.core.network.selector.PortSelector.findAvailableTcpPort;

public interface TarantoolModuleConstants {
    String TARANTOOL_MODULE_ID = "TARANTOOL_MODULE";
    String DEFAULT_TARANTOOL_EXECUTABLE = "tarantool";
    int DEFAULT_TARANTOOL_PROBE_CONNECTION_TIMEOUT = 3 * 1000;
    int DEFAULT_TARANTOOL_CONNECTION_TIMEOUT = DEFAULT_TARANTOOL_PROBE_CONNECTION_TIMEOUT * 20;
    int DEFAULT_STARTUP_TIMEOUT_SECONDS = 1000;
    int DEFAULT_TARANTOOL_PORT = findAvailableTcpPort();
    String DEFAULT_TARANTOOL_USERNAME = "guest";
    String LUA_REGEX = ".+lua";
    String JTW_EXTENSION = ".jtw";
    String IS_NULLABLE = "is_nullable";
    String COLLATION = "collation";
    String ID = "id";
    String VALUE = "value";

    interface Templates {
        String CONFIGURATION = "configuration.lua";
        String CREATE_INDEX = "create_index.lua";
        String ALTER_INDEX = "alter_index.lua";
        String CREATE_SPACE = "create_space.lua";
        String CREATE_SEQUENCE = "create_sequence.lua";
        String FORMAT_SPACE = "format_space.lua";
        String USER = "user.lua";
        String VALUE = "value.lua";
        String COMMON = "common.lua";
        String SIMPLE_VALUE = "simple_value.lua";
        String SPACE_MANAGEMENT = "manage_space.lua";
        String INDEX_MANAGEMENT = "manage_index.lua";
        String SEQUENCE_MANAGEMENT = "manage_sequence.lua";
    }

    interface Scripts {
        String INITIALIZATION = "initialization.lua";
    }

    interface Directories {
        String BIN = "bin";
        String LUA = "lua";
    }

    interface ExceptionMessages {
        String CONFIGURATION_IS_NULL = "Tarantool ''{0}'' configuration is null. Please specify it.";
        String ENTITY_MAPPING_IS_NULL = "Tarantool ''{0}'' entity mapping is null. Please specify it.";
        String UNABLE_TO_CONNECT_TO_TARANTOOL = "Unable to connect to tarantool ''{0}'' with address ''{1}''. Connection waiting time has passed";
        String TARANTOOL_EXECUTABLE_NOT_EXISTS = "Tarantool ''{0}'' executable not exists inside classpath";
        String TARANTOOL_INITIALIZATION_SCRIP_NOT_EXISTS = "Tarantool ''{0}'' initialization script not exists inside classpath";
        String ENTITY_WITHOUT_ID_FILED = "Entity ''{0}'' does not has 'id' long field";
        String ENTITY_IS_NULL = "Entity ''{0}'' is null";
    }

    interface LoggingMessages {
        String TARANTOOL_SUCCESSFULLY_CONNECTED = "Tarantool ''{0}'' with address ''{1}'' successfully connected";
        String TARANTOOL_SUCCESSFULLY_STARTED = "Tarantool ''{0}'' with address ''{1}'' successfully started";
        String WRITING_TARANTOOL_CONFIGURATION = "Writing Tarantool ''{0}'' address ''{1}'' configuration:\n\t{2}\nto file ''{3}''";
        String EVALUATING_LUA_SCRIPT = "Evaluating lua script:\n\t{0}";
        String EXTRACT_TARANTOOL_EXECUTABLE = "Extract Tarantool ''{0}'' with address ''{1}'' executable ''{2}'' file to ''{3}''";
        String EXTRACT_TARANTOOL_LUA_SCRIPTS = "Extract Tarantool ''{0}'' with address''{1}'' lua scripts to ''{2}''";
        String WRITING_TARANTOOL_USER_CONFIGURATION = "Writing Tarantool ''{0}'' with address = ''{1}'' user configuration to file ''{1}''";
        String UNABLE_TO_CONNECT_TO_TARANTOOL_ON_STARTUP = "Unable to connect to tarantool ''{0}'' with address ''{1}'' on startup. Therefore, we will try to run the tarantool";
        String CALLING_FUNCTION = "Calling tarantool function ''{0}'' with arguments: {1}";
        String CALLED_FUNCTION = "Called tarantool function ''{0}'' with result: {1}";
        String FAILED_FUNCTION = "Failed to call tarantool function ''{0}''";
    }

    interface TemplateParameterKeys {
        String LISTEN = "listen";
        String BACKGROUND = "background";
        String CUSTOM_PROC_TITLE = "customProcTitle";
        String MEMTX_DIR = "memtxDir";
        String VINYL_DIR = "vinylDir";
        String WORK_DIR = "workDir";
        String USERNAME = "username";
        String PID_FILE = "pidFile";
        String READ_ONLY = "readOnly";
        String VINYL_TIMEOUT = "vinylTimeout";
        String WORKER_POOL_THREADS = "workerPoolThreads";
        String PASSWORD = "password";
        String INDEX_NAME = "indexName";
        String SPACE_NAME = "spaceName";
        String SEQUENCE_NAME = "sequenceName";
        String PARTS = "parts";
        String TYPE = "type";
        String UNIQUE = "unique";
        String DIMENSION = "dimension";
        String DISTANCE = "distance";
        String BLOOM_FPR = "bloomFpr";
        String PAGE_SIZE = "pageSize";
        String RANGE_SIZE = "rangeSize";
        String RUN_COUNT_PER_LEVEL = "runCountPerLevel";
        String RUN_SIZE_RATIO = "runSizeRatio";
        String SEQUENCE = "sequence";
        String TEMPORARY = "temporary";
        String ENGINE = "engine";
        String FIELD_COUNT = "fieldCount";
        String FORMAT = "format";
        String IS_LOCAL = "isLocal";
        String USER = "user";
        String START = "start";
        String MIN = "min";
        String MAX = "max";
        String CYCLE = "cycle";
        String CACHE = "cache";
        String STEP = "step";
        String MEMTX_MAX_TUPLE_SIZE = "memtxMaxTupleSize";
        String MEMTX_MEMORY = "memtxMemory";
        String SLAB_ALLOC_FACTOR = "slabAllocFactor";
    }

    interface Functions {
        String PUT = "put";
        String GET = "get";
        String BY = "By";
        String SELECT = "select";
        String DELETE = "delete";
        String DELETE_ALL = "deleteAll";
        String TRUNCATE = "truncate";
        String COUNT = "count";
        String LEN = "len";
        String DROP = "drop";
        String RENAME = "rename";
        String NEXT = "next";
        String SET = "set";
        String RESET = "reset";
        String INSERT = "insert";
        String UPDATE = "update";
        String UPSERT = "upsert";
        String VALUES_POSTFIX = "Values";
        String VALUE_POSTFIX = "Value";
        String WITH_SCHEMA_POSTFIX = "WithSchema";
    }

    enum TarantoolInstanceMode {
        LOCAL,
        REMOTE
    }

    enum TarantoolInitializationMode {
        ON_MODULE_LOAD,
        MANUAL
    }

    enum TarantoolFieldType {
        UNSIGNED,
        STRING,
        INTEGER,
        NUMBER,
        BOOLEAN,
        ARRAY,
        SCALAR
    }

    enum TarantoolIndexType {
        HASH,
        TREE,
        BITSET,
        RTREE
    }

    enum TarantoolIdCalculationMode {
        MANUAL,
        SEQUENCE
    }

    @Getter
    @AllArgsConstructor
    enum TarantoolOperator {
        ADDITION("+"),
        SUBTRACTION("-"),
        AND("&"),
        OR("|"),
        XOR("^"),
        STRING_SPLICE(":"),
        INSERTION("!"),
        DELETION("#"),
        ASSIGMENT("=");

        private final String operator;
    }
}