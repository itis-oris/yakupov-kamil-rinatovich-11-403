package com.arsaka.search.request.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record FlightsSearchFilter(
        Boolean isBaggageIncluded,

        Boolean isRefundable,

        TimeType scheduledDepartureTimeType,

        TimeType scheduledArrivalTimeType,

        @Size(min = 1)
        Set<@NotBlank @Pattern(regexp = "^[A-Z0-9]{2}$") String> airlineCodes,

        @Size(min = 1)
        Set<@NotBlank @Pattern(regexp = "^[A-Z]{3}$") String> airportDepartureCodes,

        @Size(min = 1)
        Set<@NotBlank @Pattern(regexp = "^[A-Z]{3}$")String> airportArrivalCodes,

        @Valid
        PriceRange priceRange,

        @Size(min = 1)
        Set<@NotBlank @Pattern(regexp = "^[A-Z0-9]{3}$") String> airplaneTypeCodes,

        OrderType orderType
) {
}
