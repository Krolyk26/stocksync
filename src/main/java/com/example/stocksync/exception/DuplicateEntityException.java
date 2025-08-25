package com.example.stocksync.exception;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String entity, String reason) {
        super(entity + " duplicate: " + reason);
    }
}