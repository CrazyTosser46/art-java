package ru.adk.tarantool.initializer;

import org.apache.logging.log4j.Logger;
import org.jtwig.JtwigModel;
import org.tarantool.TarantoolClient;
import org.zeroturnaround.exec.ProcessExecutor;
import ru.adk.tarantool.configuration.TarantoolConfiguration;
import ru.adk.tarantool.configuration.TarantoolConnectionConfiguration;
import ru.adk.tarantool.configuration.TarantoolLocalConfiguration;
import ru.adk.tarantool.exception.TarantoolConnectionException;
import ru.adk.tarantool.exception.TarantoolInitializationException;
import ru.adk.tarantool.module.TarantoolModule;
import static java.io.File.separator;
import static java.lang.Thread.sleep;
import static java.nio.file.Paths.get;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static org.jtwig.JtwigTemplate.classpathTemplate;
import static ru.adk.core.constants.StringConstants.COLON;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.core.extension.FileExtensions.writeFile;
import static ru.adk.core.jar.JarExtensions.extractCurrentJarEntry;
import static ru.adk.core.jar.JarExtensions.insideJar;
import static ru.adk.logging.LoggingModule.loggingModule;
import static ru.adk.tarantool.connector.TarantoolConnector.connectToTarantool;
import static ru.adk.tarantool.connector.TarantoolConnector.tryConnectToTarantool;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Directories.BIN;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Directories.LUA;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.*;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Scripts.INITIALIZATION;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.TarantoolInstanceMode.LOCAL;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.PASSWORD;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.USERNAME;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Templates.CONFIGURATION;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Templates.USER;
import static ru.adk.tarantool.module.TarantoolModule.tarantoolModule;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;

public class TarantoolInitializer {
    private final static OutputStream TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM = forLogger(loggingModule()
            .getLogger(TarantoolInitializer.class))
            .buildOutputStream();
    private final static Logger logger = loggingModule().getLogger(TarantoolInitializer.class);

    @SuppressWarnings("Duplicates")
    public static void initializeTarantool(String instanceId) {
        TarantoolConfiguration tarantoolConfiguration = tarantoolModule()
                .getTarantoolConfigurations()
                .get(instanceId);
        if (isNull(tarantoolConfiguration)) {
            throw new TarantoolConnectionException(format(CONFIGURATION_IS_NULL, instanceId));
        }
        TarantoolInstanceMode instanceMode = tarantoolConfiguration.getInstanceMode();
        TarantoolConnectionConfiguration connectionConfiguration = tarantoolConfiguration.getConnectionConfiguration();
        String address = connectionConfiguration.getHost() + COLON + connectionConfiguration.getPort();
        try {
            TarantoolClient tarantoolClient = tryConnectToTarantool(instanceId);
            if (tarantoolClient.isAlive()) {
                connectToTarantool(instanceId);
                return;
            }
        } catch (Exception e) {
            if (instanceMode == LOCAL) {
                logger.warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_ON_STARTUP, instanceId, address));
                startTarantool(instanceId);
            }
            connectToTarantool(instanceId);
            return;
        }
        if (instanceMode == LOCAL) {
            logger.warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_ON_STARTUP, instanceId, address));
            startTarantool(instanceId);
        }
        connectToTarantool(instanceId);
    }

    @SuppressWarnings("Duplicates")
    private static void startTarantool(String instanceId) {
        try {
            TarantoolConfiguration configuration = tarantoolModule()
                    .getTarantoolConfigurations()
                    .get(instanceId);
            if (isNull(configuration)) {
                throw new TarantoolConnectionException(format(CONFIGURATION_IS_NULL, instanceId));
            }
            TarantoolConnectionConfiguration connectionConfiguration = configuration.getConnectionConfiguration();
            TarantoolLocalConfiguration localConfiguration = tarantoolModule().getLocalConfiguration();
            String luaConfiguration = configuration.getInitialConfiguration().toLua();
            Path luaConfigurationPath = get(getLuaScriptPath(localConfiguration, CONFIGURATION));
            writeFile(luaConfigurationPath, luaConfiguration);
            String address = connectionConfiguration.getHost() + COLON + connectionConfiguration.getPort();
            loggingModule()
                    .getLogger()
                    .info(format(WRITING_TARANTOOL_CONFIGURATION, instanceId,
                            address,
                            luaConfiguration,
                            luaConfigurationPath.toAbsolutePath()));

            String luaUserConfiguration = classpathTemplate(USER + JTW_EXTENSION)
                    .render(new JtwigModel()
                            .with(USERNAME, connectionConfiguration.getUsername())
                            .with(PASSWORD, connectionConfiguration.getPassword()));
            Path userConfigurationPath = get(getLuaScriptPath(localConfiguration, USER));
            writeFile(userConfigurationPath, luaUserConfiguration);
            loggingModule()
                    .getLogger()
                    .info(format(WRITING_TARANTOOL_USER_CONFIGURATION,
                            instanceId,
                            address,
                            userConfigurationPath.toAbsolutePath()));
            URL tarantoolExecutableResource = TarantoolModule.class
                    .getClassLoader()
                    .getResource(localConfiguration.getExecutableApplicationName());
            if (isNull(tarantoolExecutableResource)) {
                throw new TarantoolInitializationException(format(TARANTOOL_EXECUTABLE_NOT_EXISTS, instanceId));
            }

            if (insideJar()) {
                extractCurrentJarEntry(localConfiguration.getExecutableApplicationName(), getBinaryPath(localConfiguration, EMPTY_STRING));
                loggingModule()
                        .getLogger()
                        .info(format(EXTRACT_TARANTOOL_EXECUTABLE,
                                instanceId,
                                address,
                                localConfiguration.getExecutableApplicationName(),
                                getBinaryPath(localConfiguration, EMPTY_STRING)));

                extractCurrentJarEntry(LUA_REGEX, getLuaScriptPath(localConfiguration, EMPTY_STRING));
                loggingModule()
                        .getLogger()
                        .info(format(EXTRACT_TARANTOOL_LUA_SCRIPTS,
                                instanceId,
                                address,
                                localConfiguration.getWorkingDirectory() + separator + LUA));

                String binaryPath = getBinaryPath(localConfiguration, localConfiguration.getExecutableApplicationName());
                String initializationScriptPath = getLuaScriptPath(localConfiguration, INITIALIZATION);
                new ProcessExecutor()
                        .command(binaryPath, initializationScriptPath)
                        .directory(new File(localConfiguration.getWorkingDirectory()))
                        .redirectOutput(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                        .redirectError(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                        .start();
                sleep(localConfiguration.getStartupTimeoutSeconds());
                loggingModule()
                        .getLogger()
                        .info(format(TARANTOOL_SUCCESSFULLY_STARTED, instanceId, address));
                return;
            }

            URL initializationResource = TarantoolInitializer.class
                    .getClassLoader()
                    .getResource(INITIALIZATION);
            if (isNull(initializationResource)) {
                throw new TarantoolInitializationException(format(TARANTOOL_INITIALIZATION_SCRIP_NOT_EXISTS, instanceId));
            }

            new ProcessExecutor()
                    .command(tarantoolExecutableResource.getPath(), initializationResource.getFile())
                    .directory(new File(localConfiguration.getWorkingDirectory()))
                    .redirectOutput(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                    .redirectError(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                    .start();
            sleep(localConfiguration.getStartupTimeoutSeconds());
            loggingModule()
                    .getLogger(TarantoolInitializer.class)
                    .info(format(TARANTOOL_SUCCESSFULLY_STARTED, instanceId, address));
        } catch (Exception e) {
            throw new TarantoolInitializationException(e);
        }
    }

    private static String getLuaScriptPath(TarantoolLocalConfiguration localConfiguration, String scriptName) {
        return localConfiguration.getWorkingDirectory() + separator + LUA + separator + scriptName;
    }

    private static String getBinaryPath(TarantoolLocalConfiguration localConfiguration, String binaryName) {
        return localConfiguration.getWorkingDirectory() + separator + BIN + separator + binaryName;
    }
}
