package com.oris.search.response;

import com.oris.search.response.dto.*;

import java.util.UUID;

public record FlightSearchResponse(
        UUID flightId,
        FlightsSearchAirline airline,
        FlightsSearchAirport departureAirport,
        FlightsSearchAirport arrivalAirport,
        FlightsSearchAirplaneType airplaneType,
        String scheduledDeparture,
        String scheduledArrival,
        FlightSeat seat,
        FlightFare fare,
        FlightPassengerTypePrice farePrice
) {
}
