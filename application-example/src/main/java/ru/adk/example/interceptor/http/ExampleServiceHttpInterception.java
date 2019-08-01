package ru.adk.example.interceptor.http;

import ru.adk.core.constants.InterceptionStrategy;
import ru.adk.http.server.interceptor.HttpServerInterception;
import static ru.adk.core.constants.InterceptionStrategy.NEXT_INTERCEPTOR;
import static ru.adk.logging.LoggingModule.loggingModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Http interceptors can do some logic before executing http service method and after that
 */
public class ExampleServiceHttpInterception implements HttpServerInterception {

    /**
     * This method processes http requests and responses
     *
     * @param request  - http request with service incoming data and http request data
     * @param response - http response with service method response and http response data
     * @return InterceptionStrategy - as usual it's  NEXT_INTERCEPTOR
     */
    @Override
    public InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) {
        loggingModule().getLogger().info("Http service interceptor executed");
        return NEXT_INTERCEPTOR;
    }
}
