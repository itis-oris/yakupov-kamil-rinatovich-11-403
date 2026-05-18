package com.arsaka.auth.exception;

import lombok.Getter;

@Getter
public class AuthServiceException extends RuntimeException {
    private final int status;
    private final ApiException apiException;

    public AuthServiceException(int status, ApiException apiException) {
        this.status = status;
        this.apiException = apiException;
    }
}