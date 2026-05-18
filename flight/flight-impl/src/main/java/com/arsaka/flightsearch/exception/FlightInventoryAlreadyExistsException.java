package com.arsaka.flightsearch.exception;

import com.arsaka.common.CabinClass;
import com.arsaka.exception.ConflictException;

import java.util.UUID;

public class FlightInventoryAlreadyExistsException extends ConflictException {
    public FlightInventoryAlreadyExistsException(UUID flightId, CabinClass cabinClass) {
        super("Flight inventory already exists | flight id=%s | cabin class=%s".formatted(flightId, cabinClass));
    }
}
