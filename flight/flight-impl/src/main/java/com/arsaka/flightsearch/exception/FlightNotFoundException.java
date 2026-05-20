package com.arsaka.flightsearch.exception;

import com.arsaka.exception.NotFoundException;

public class FlightNotFoundException extends NotFoundException {
    public FlightNotFoundException() {
        super("flight not found");
    }
}
