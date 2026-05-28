package com.oris.flight.exception;

import com.oris.exception.NotFoundException;

public class FlightNotFoundException extends NotFoundException {
    public FlightNotFoundException() {
        super("flight not found");
    }
}
