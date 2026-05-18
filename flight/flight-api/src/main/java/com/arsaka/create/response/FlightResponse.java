package com.arsaka.create.response;

import com.arsaka.common.FlightStatus;
import com.arsaka.create.request.dto.Time;

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
