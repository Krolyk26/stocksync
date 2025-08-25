package com.example.stocksync.exception;

public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String source, String message, Throwable cause) {
        super(source + " error: " + message, cause);
    }
    public ExternalServiceException(String source, String message) {
        super(source + " error: " + message);
    }
}