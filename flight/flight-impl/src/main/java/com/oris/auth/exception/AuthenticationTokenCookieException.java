package com.oris.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationTokenCookieException extends AuthenticationException {

    public AuthenticationTokenCookieException(String msg) {
        super(msg);
    }
}