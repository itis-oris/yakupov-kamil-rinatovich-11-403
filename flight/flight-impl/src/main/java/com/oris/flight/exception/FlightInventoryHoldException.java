package com.oris.flight.exception;

import com.oris.exception.BadRequestException;

public class FlightInventoryHoldException extends BadRequestException {
    public FlightInventoryHoldException() {
        super("Flight Inventory is not available");
    }
}
