package com.oris.updaterequest;

import com.oris.common.FlightStatus;
import com.oris.create.request.dto.Time;

import java.util.UUID;

public record UpdateFlightRequest(
        UUID airplaneId,
        Time scheduledTime,
        Time actualTime,
        FlightStatus status
) {
}
