package com.arsaka.create.response;

public record AirlineResponse(
        String code,
        String name,
        String countryCode,
        boolean active
) {
}
