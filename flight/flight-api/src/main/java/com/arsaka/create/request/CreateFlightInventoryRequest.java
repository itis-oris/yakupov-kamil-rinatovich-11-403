package com.arsaka.create.request;

import com.arsaka.common.CabinClass;
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
