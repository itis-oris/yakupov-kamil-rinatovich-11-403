package com.arsaka.search.response.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record FlightSearch(
    UUID flightId,
    FlightsSearchAirport departureAirport,
    FlightsSearchAirport arrivalAirport,
    String scheduledDeparture,
    String scheduledArrival,
    BigDecimal price
) {
}
