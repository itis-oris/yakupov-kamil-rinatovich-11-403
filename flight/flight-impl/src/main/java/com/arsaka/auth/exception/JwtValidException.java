package com.arsaka.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtValidException extends AuthenticationException {
    public JwtValidException(String message) {
        super(message);
    }
}
