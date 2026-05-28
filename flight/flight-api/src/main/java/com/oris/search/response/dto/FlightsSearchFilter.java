package com.oris.search.response.dto;

import java.math.BigDecimal;
import java.util.Set;

public record FlightsSearchFilter(
        Set<FlightsSearchAirline> airlines,
        Set<FlightsSearchAirport> departureAirports,
        Set<FlightsSearchAirport> arrivalAirports,
        Set<FlightsSearchAirplaneType> airplaneTypes,
        BigDecimal priceBegin,
        BigDecimal priceEnd
) {
}
