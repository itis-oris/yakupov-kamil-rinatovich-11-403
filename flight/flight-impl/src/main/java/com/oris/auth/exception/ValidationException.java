package com.oris.auth.exception;

public class ValidationException extends RuntimeException {
    public ValidationException() {
        super("Request validation failed, please try again later");
    }
}
