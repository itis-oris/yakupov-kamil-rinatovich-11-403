package com.arsaka.flightsearch.exception;

import com.arsaka.exception.NotFoundException;

import java.util.UUID;

public class FlightNotFoundException extends NotFoundException {
    public FlightNotFoundException(UUID flightId) {
        super("flight not found | flight id=%s".formatted(flightId));
    }
}
