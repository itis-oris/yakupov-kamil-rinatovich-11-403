package com.arsaka.flightsearch.exception;

import com.arsaka.common.CabinClass;
import com.arsaka.exception.NotFoundException;

import java.util.UUID;

public class FlightInventoryNotFoundException extends NotFoundException {
    public FlightInventoryNotFoundException(UUID flightId, CabinClass cabinClass) {
        super("flight inventory not found | flight id=%s | cabin class=%s".formatted(flightId, cabinClass));
    }

    public FlightInventoryNotFoundException(UUID id) {
        super("flight inventory not found | id=%s".formatted(id));
    }
}
