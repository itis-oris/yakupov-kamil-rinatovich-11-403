package com.arsaka.updaterequest;

import com.arsaka.common.FlightStatus;
import com.arsaka.create.request.dto.Time;

import java.util.UUID;

public record UpdateFlightRequest(
        UUID airplaneId,
        Time scheduledTime,
        Time actualTime,
        FlightStatus status
) {
}
