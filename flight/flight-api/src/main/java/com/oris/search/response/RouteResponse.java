package com.oris.search.response;

import com.oris.search.response.dto.FlightsSearchAirline;
import com.oris.search.response.dto.FlightsSearchAirport;

import java.util.UUID;

public record RouteResponse(
        UUID id,
        String number,
        FlightsSearchAirline airline,
        FlightsSearchAirport departureAirport,
        FlightsSearchAirport arrivalAirport,
        boolean isActive
) {}
