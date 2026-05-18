package com.arsaka.search.response;

import com.arsaka.search.response.dto.FlightsSearchAirline;
import com.arsaka.search.response.dto.FlightsSearchAirport;

import java.util.UUID;

public record RouteResponse(
        UUID id,
        String number,
        FlightsSearchAirline airline,
        FlightsSearchAirport departureAirport,
        FlightsSearchAirport arrivalAirport,
        boolean isActive
) {}
