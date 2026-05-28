package com.oris.create.response;

import java.util.UUID;

public record AirplaneResponse(
        UUID id,
        String number,
        String airplaneTypeCode,
        String airlineCode,
        int manufacturedYear,
        boolean active
) {
}
