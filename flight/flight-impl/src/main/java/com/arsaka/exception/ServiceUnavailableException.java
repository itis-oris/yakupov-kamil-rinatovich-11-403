package com.arsaka.exception;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends ServiceException {
    public ServiceUnavailableException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
