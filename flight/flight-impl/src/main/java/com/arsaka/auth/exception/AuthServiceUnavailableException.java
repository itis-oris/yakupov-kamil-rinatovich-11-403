package com.arsaka.auth.exception;

import com.arsaka.exception.ServiceUnavailableException;

public class AuthServiceUnavailableException extends ServiceUnavailableException {
    public AuthServiceUnavailableException() {
        super("Authentication service unavailable, please try again later");
    }
}