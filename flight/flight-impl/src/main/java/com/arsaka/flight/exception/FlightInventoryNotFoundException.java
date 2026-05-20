package com.arsaka.flight.exception;

import com.arsaka.exception.NotFoundException;

public class FlightInventoryNotFoundException extends NotFoundException {
    public FlightInventoryNotFoundException() {
        super("flight inventory not found");
    }
}
