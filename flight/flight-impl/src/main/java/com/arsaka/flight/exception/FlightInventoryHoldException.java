package com.arsaka.flight.exception;

import com.arsaka.exception.BadRequestException;

public class FlightInventoryHoldException extends BadRequestException {
    public FlightInventoryHoldException() {
        super("Flight Inventory is not available");
    }
}
