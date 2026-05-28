package com.oris.search.request;

import jakarta.validation.constraints.Pattern;import jakarta.validation.constraints.Positive;

public record RouteFilter(
        @Pattern(regexp = "^[A-Z0-9]{2}$")
        String airlineCode,

        @Pattern(regexp = "^[A-Z]{3}$")
        String departureAirportCode,

        @Pattern(regexp = "^[A-Z]{3}$")
        String arrivalAirportCode,

        @Pattern(regexp = "^[A-Z]{2}$")
        String departureCountryCode,

        @Pattern(regexp = "^[A-Z]{2}$")
        String arrivalCountryCode,

        @Pattern(regexp = "^[A-Z]{3}$")
        String cityCode,

        Boolean active,

        @Positive
        Integer limit
) {}
