package com.arsaka.auth.exception;

public class InvalidSessionException extends RuntimeException {
    public InvalidSessionException() {
        super("Invalid session");
    }
}
