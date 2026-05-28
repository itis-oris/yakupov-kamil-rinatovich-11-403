package com.oris.auth.exception;

import com.oris.exception.ServiceUnavailableException;

public class AuthServiceUnavailableException extends ServiceUnavailableException {
    public AuthServiceUnavailableException() {
        super("Authentication service unavailable, please try again later");
    }
}