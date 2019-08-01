package ru.adk.configurator.configuration;

import lombok.Getter;
import org.zalando.logbook.Logbook;
import ru.adk.configurator.dao.UserDao;
import ru.adk.http.mapper.HttpContentMapper;
import ru.adk.http.mime.MimeType;
import ru.adk.http.server.HttpServerModuleConfiguration.HttpServerModuleDefaultConfiguration;
import ru.adk.http.server.interceptor.CookieInterceptor;
import ru.adk.http.server.interceptor.HttpServerInterceptor;
import static ru.adk.config.ConfigProvider.config;
import static ru.adk.config.constants.ConfigType.YAML;
import static ru.adk.configurator.constants.ConfiguratorModuleConstants.AUTHORIZATION_CHECKING_URLS;
import static ru.adk.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.*;
import static ru.adk.configurator.constants.ConfiguratorModuleConstants.TOKEN_COOKIE;
import static ru.adk.configurator.http.content.mapping.ConfiguratorHttpContentMapping.configureContentMappers;
import static ru.adk.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.adk.http.constants.HttpStatus.UNAUTHORIZED;
import static ru.adk.http.server.HttpServerModuleConfiguration.initializeWebServerInterceptors;
import static ru.adk.http.server.HttpServerModuleConfiguration.logbookWithoutWebLogs;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.*;
import static ru.adk.http.server.interceptor.HttpServerInterceptor.intercept;
import static ru.adk.http.server.service.HttpWebResourceService.getStringResource;
import static ru.adk.metrics.http.filter.MetricsHttpLogFilter.logbookWithoutMetricsLogs;
import java.util.List;
import java.util.Map;

@Getter
public class ConfiguratorHttpServerConfiguration extends HttpServerModuleDefaultConfiguration {
    private final Map<MimeType, HttpContentMapper> contentMappers = configureContentMappers(super.getContentMappers());
    private final int port = config(CONFIGURATOR_SECTION_ID, YAML).asYamlConfig().getInt(CONFIGURATOR_HTTP_PORT_PROPERTY);
    @Getter(lazy = true)
    private final List<HttpServerInterceptor> requestInterceptors = initializeRequestInterceptors(super.getRequestInterceptors());
    @Getter(lazy = true)
    private final HttpWebConfiguration webConfiguration = HttpWebConfiguration.builder()
            .resourceBufferSize(DEFAULT_BUFFER_SIZE)
            .templateResourceVariables(URL_TEMPLATE_VARIABLE, (url) -> config(CONFIGURATOR_SECTION_ID).asYamlConfig().getString(CONFIGURATOR_WEB_URL_PROPERTY))
            .build();
    @Getter(lazy = true)
    private final Logbook logbook = logbookWithoutMetricsLogs(logbookWithoutWebLogs()).build();
    @Getter(lazy = true)
    private final String path = config(CONFIGURATOR_SECTION_ID).asYamlConfig().getString(CONFIGURATOR_WEB_URL_PROPERTY);

    private static List<HttpServerInterceptor> initializeRequestInterceptors(List<HttpServerInterceptor> superInterceptors) {
        List<HttpServerInterceptor> httpServerInterceptors = dynamicArrayOf(initializeWebServerInterceptors(superInterceptors));
        httpServerInterceptors.add(intercept(CookieInterceptor.builder()
                .checkingUrls(AUTHORIZATION_CHECKING_URLS)
                .cookieValue(TOKEN_COOKIE, UserDao::getToken)
                .errorStatus(UNAUTHORIZED.getCode())
                .errorContent(getStringResource(INDEX_HTML))
                .build()));
        return httpServerInterceptors;
    }
}