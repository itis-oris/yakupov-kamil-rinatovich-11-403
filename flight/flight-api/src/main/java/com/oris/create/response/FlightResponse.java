package com.oris.create.response;

import com.oris.common.FlightStatus;
import com.oris.create.request.dto.Time;

import java.util.UUID;

public record FlightResponse(
        UUID id,
        UUID routeId,
        String airplaneTypeCode,
        UUID airplaneId,
        Time scheduledTime,
        Time actualTime,
        FlightStatus status
) {
}
