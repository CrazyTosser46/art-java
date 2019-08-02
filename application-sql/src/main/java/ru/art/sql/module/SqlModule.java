package ru.art.sql.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.sql.configuration.SqlModuleConfiguration;
import ru.art.sql.configuration.SqlModuleConfiguration.SqlModuleDefaultConfiguration;
import ru.art.sql.exception.SqlModuleException;
import static java.lang.Class.forName;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.sql.constants.SqlModuleConstants.SQL_MODULE_ID;
import javax.sql.DataSource;

@Getter
public class SqlModule implements Module<SqlModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final SqlModuleConfiguration sqlModule = context().getModule(SQL_MODULE_ID, new SqlModule());
    private final String id = SQL_MODULE_ID;
    private final SqlModuleConfiguration defaultConfiguration = new SqlModuleDefaultConfiguration();

    public static SqlModuleConfiguration sqlModule() {
        return getSqlModule();
    }

    @Override
    public void onLoad() {
        DataSource dataSource = null;
        switch (sqlModule().getConnectionPoolType()) {
            case HIKARI:
                dataSource = sqlModule().getHikariPoolConfig().getDataSource();
                break;
            case TOMCAT:
                dataSource = new org.apache.tomcat.jdbc.pool.DataSource(sqlModule().getTomcatPoolConfig());
                break;
        }
        try {
            forName(sqlModule().getDbProvider().getDriverClassName());
            sqlModule().getJooqConfiguration().set(dataSource);
        } catch (Exception e) {
            throw new SqlModuleException(e);
        }

    }
}