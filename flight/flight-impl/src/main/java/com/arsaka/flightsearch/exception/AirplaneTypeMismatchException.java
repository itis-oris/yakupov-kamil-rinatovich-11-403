package com.arsaka.flightsearch.exception;

import com.arsaka.exception.ConflictException;

public class AirplaneTypeMismatchException extends ConflictException {
    public AirplaneTypeMismatchException() {
        super("Assigned airplane type does not match flight airplane type");
    }
}
