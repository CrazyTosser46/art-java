package ru.adk.grpc.exception;

public class GrpcException extends RuntimeException {
    public GrpcException(String message) {
        super(message);
    }
}
