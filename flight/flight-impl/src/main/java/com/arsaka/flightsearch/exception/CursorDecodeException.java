package com.arsaka.flightsearch.exception;

public class CursorDecodeException extends RuntimeException {
    public CursorDecodeException(String cursor) {
        super("Failed to decode cursor | cursor=%s".formatted(cursor));
    }
}
