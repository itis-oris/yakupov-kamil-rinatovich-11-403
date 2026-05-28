package com.oris.create.response;

import com.oris.common.CabinClass;

import java.util.UUID;

public record FareResponse(
        UUID id,
        String airlineCode,
        CabinClass cabinClass,
        String name,
        boolean baggageIncluded,
        boolean refundable,
        boolean active
) {
}
