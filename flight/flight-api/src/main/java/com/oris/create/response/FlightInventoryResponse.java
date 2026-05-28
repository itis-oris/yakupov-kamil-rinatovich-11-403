package com.oris.create.response;

import com.oris.common.CabinClass;

import java.math.BigDecimal;
import java.util.UUID;

public record FlightInventoryResponse(
        UUID id,
        UUID flightId,
        CabinClass cabinClass,
        BigDecimal totalSeats,
        BigDecimal availableSeats,
        BigDecimal heldSeats,
        BigDecimal price
){
}
