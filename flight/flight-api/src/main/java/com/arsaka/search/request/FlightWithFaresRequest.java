package com.arsaka.search.request;

import com.arsaka.common.CabinClass;
import com.arsaka.common.PassengerType;
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
