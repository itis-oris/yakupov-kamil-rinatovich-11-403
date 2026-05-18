package com.arsaka.flightsearch.exception;

import com.arsaka.common.CabinClass;
import com.arsaka.exception.BadRequestException;

import java.util.UUID;

public class FlightInventoryHoldException extends BadRequestException {
    public FlightInventoryHoldException(UUID flightId, CabinClass cabinClass) {
        super("Flight Inventory is not available | flightId=%s | cabinClass=%s".formatted(flightId, cabinClass));
    }
}
