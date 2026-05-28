package com.oris.flight.exception;

import com.oris.exception.ConflictException;

public class AirplaneTypeMismatchException extends ConflictException {
    public AirplaneTypeMismatchException() {
        super("Assigned airplane type does not match flight airplane type");
    }
}
