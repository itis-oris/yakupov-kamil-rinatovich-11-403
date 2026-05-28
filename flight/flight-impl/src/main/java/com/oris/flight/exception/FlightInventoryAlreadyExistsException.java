package com.oris.flight.exception;

import com.oris.exception.ConflictException;

public class FlightInventoryAlreadyExistsException extends ConflictException {
    public FlightInventoryAlreadyExistsException() {
        super("Flight inventory already exists");
    }
}
