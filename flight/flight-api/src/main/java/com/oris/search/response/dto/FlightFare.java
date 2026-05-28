package com.oris.search.response.dto;

import com.oris.common.CabinClass;

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
