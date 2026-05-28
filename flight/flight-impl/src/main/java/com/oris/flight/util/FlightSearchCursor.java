package com.oris.flight.util;

import com.oris.search.request.dto.OrderType;

import java.util.UUID;

public record FlightSearchCursor(
        OrderType orderType,
        Object primaryValue,
        UUID flightId
) {
}
