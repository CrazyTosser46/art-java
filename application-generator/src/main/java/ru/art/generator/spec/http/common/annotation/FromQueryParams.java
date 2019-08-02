package ru.art.generator.spec.http.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ru.art.core.constants.StringConstants.EMPTY_STRING;

/**
 * Annotation for specification generator.
 */
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FromQueryParams {
    boolean httpService() default false;

    String httpProxyQueryName() default EMPTY_STRING;

    String httpProxyQueryValue() default EMPTY_STRING;
}