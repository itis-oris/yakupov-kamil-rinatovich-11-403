package com.oris.search.response;

import com.oris.common.CabinClass;
import com.oris.search.response.dto.*;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record FlightWithFaresResponse(
        UUID flightId,
        FlightsSearchAirline airline,
        FlightsSearchAirport departureAirport,
        FlightsSearchAirport arrivalAirport,
        FlightsSearchAirplaneType airplaneType,
        String scheduledDeparture,
        String scheduledArrival,
        CabinClass cabinClass,
        Set<FlightSeat> seats,
        Map<UUID, FlightPassengersTypePriceMap> farePrice,
        Set<FlightFare> fares
) {
}
