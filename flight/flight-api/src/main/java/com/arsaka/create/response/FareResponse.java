package com.arsaka.create.response;

import com.arsaka.common.CabinClass;

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
