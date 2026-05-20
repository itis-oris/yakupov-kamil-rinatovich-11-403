package com.arsaka.flight.util;

import com.arsaka.search.request.dto.OrderType;

import java.util.UUID;

public record FlightSearchCursor(
        OrderType orderType,
        Object primaryValue,
        UUID flightId
) {
}
