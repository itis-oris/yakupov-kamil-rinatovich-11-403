package com.oris.search.request;

import com.oris.common.CabinClass;
import com.oris.common.PassengerType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Map;

public record FlightWithFaresRequest(
        @NotEmpty
        Map<@NotNull PassengerType, @Positive Integer> passengers,

        @NotNull
        CabinClass cabinClass
) {
}
