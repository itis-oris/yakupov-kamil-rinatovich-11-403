package com.oris.event;

import com.oris.event.dto.FlightHold;

import java.util.Set;

public record FlightsHoldEventRequest(
        Set<FlightHold> flights
) {
}
