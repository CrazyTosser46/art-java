package ru.adk.rocks.db.exception;

public class RocksDbOperationException extends RuntimeException {
    public RocksDbOperationException(String message, Exception exception) {
        super(message, exception);
    }
}
