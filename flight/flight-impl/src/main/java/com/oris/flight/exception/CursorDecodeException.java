package com.oris.flight.exception;

public class CursorDecodeException extends RuntimeException {
    public CursorDecodeException() {
        super("Failed to decode cursor");
    }
}
