package com.arsaka.flightsearch.exception;

import com.arsaka.exception.NotFoundException;

public class FlightInventoryNotFoundException extends NotFoundException {
    public FlightInventoryNotFoundException() {
        super("flight inventory not found");
    }
}
