package com.example.stocksync.exception;

public class NotFoundEntityException extends RuntimeException {
    public NotFoundEntityException(String entity, Object idOrKey) {
        super(entity + " not found: " + idOrKey);
    }
}