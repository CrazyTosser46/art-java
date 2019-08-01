package ru.adk.generator.spec.common.constants;

/**
 * Interface containing constants for all specification's generators.
 */
public interface CommonSpecGeneratorConstants {
    String SPECIFICATION = "specification";
    String CAST = "cast";
    String METHOD_NAME_STRING = "Method ''{0}'': ";
    String LAZY = "lazy";
    String FLUENT = "fluent";

    String REGEXP_UPPER_CASE_LETTERS = "[A-Z]";
    String REGEXP_LOWER_CASE_LETTERS = "[a-z]";
    String SERVICE_ID = "serviceId";
    String JAVADOC = "Specification for {@link $T}";

    String INTERCEPT_REQUEST = "interceptRequest";
    String INTERCEPT_RESPONSE = "interceptResponse";

    interface PathAndPackageConstants {
        String SLASH_SPEC_SLASH = "\\spec\\";
        String SLASH_SERVICE_SLASH = "\\service\\";
        String SPEC = "spec";
    }

    interface AnnotationParametersToMethods {
        String ADD_REQUEST_INTERCEPTOR = ".addReqInterceptor($N(new $T()))";
        String ADD_RESPONSE_INTERCEPTOR = ".addRespInterceptor($N(new $T()))";
        String PREPARE_METHOD = ".prepare()";
        String RESP_MAPPER = ".responseMapper($N)";
        String REQ_MAPPER = ".requestMapper($N)";
        String WITH_PATH_PARAMETER = ".withPathParameter($S)";
        String WITH_QUERY_PARAMETER = ".withQueryParameter($S, $S)";
        String CONSUMES = ".consumes($N)";
        String PRODUCES = ".produces($N)";
    }

    interface ExecuteMethodConstants {
        String EXECUTE_METHOD = "executeMethod";
        String METHOD_ID = "methodId";
        String REQ = "request";
        String TYPE_P = "P";
        String TYPE_R = "R";

        String SWITCH_BY_METHOD_ID = "switch (methodId)";
        String DEFAULT_IN_EXEC_METHOD = "default:\nthrow new $T(serviceId, methodId)";

        String EXEC_REQ_SERVICE_SPEC = "case $L:\n$N(($T) request);\nreturn null";
        String EXEC_RESP_AND_REQ_SERVICE_SPEC = "case $L:\nreturn cast($N(($T) request))";
        String EXEC_RESP_SERVICE_SPEC = "case $L:\nreturn cast($N())";
        String EXEC_NO_RESP_OR_REQ_SERVICE_SPEC = "case $L:\n$N();\nreturn null";
    }
}
