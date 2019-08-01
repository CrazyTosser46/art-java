package ru.adk.core.factory;

@FunctionalInterface
public interface ExceptionFactory<T extends RuntimeException> {
    T create(Exception e);
}
