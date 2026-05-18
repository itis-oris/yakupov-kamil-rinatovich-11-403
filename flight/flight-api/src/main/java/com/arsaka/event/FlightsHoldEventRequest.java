package com.arsaka.event;

import com.arsaka.event.dto.FlightHold;

import java.util.Set;

public record FlightsHoldEventRequest(
        Set<FlightHold> flights
) {
}
