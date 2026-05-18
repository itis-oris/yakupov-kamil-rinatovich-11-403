package com.arsaka.search.response.dto;

import com.arsaka.common.CabinClass;

import java.util.UUID;

public record FlightFare(
    UUID fareId,
    String airlineCode,
    CabinClass cabinClass,
    String name,
    boolean isBaggageIncluded,
    boolean isRefundable
) {
}
