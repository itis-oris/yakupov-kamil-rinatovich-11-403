package com.oris.create.request;

import com.oris.common.CabinClass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateFlightInventoryRequest(
        @NotNull
        UUID flightId,

        @NotNull
        CabinClass cabinClass,

        @NotNull
        @PositiveOrZero
        BigDecimal price
) {
}
