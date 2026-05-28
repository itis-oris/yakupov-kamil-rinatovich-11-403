package com.oris.flight.exception;

import com.oris.exception.NotFoundException;

public class FlightInventoryNotFoundException extends NotFoundException {
    public FlightInventoryNotFoundException() {
        super("flight inventory not found");
    }
}
