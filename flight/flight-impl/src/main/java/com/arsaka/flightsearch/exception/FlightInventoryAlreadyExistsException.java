package com.arsaka.flightsearch.exception;

import com.arsaka.exception.ConflictException;

public class FlightInventoryAlreadyExistsException extends ConflictException {
    public FlightInventoryAlreadyExistsException() {
        super("Flight inventory already exists");
    }
}
