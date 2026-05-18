package com.arsaka.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class ValidationException extends RuntimeException {
    public ValidationException() {
        super("Request validation failed, please try again later");
    }
}
