package com.arsaka.search.response;

import com.arsaka.search.response.dto.*;

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
