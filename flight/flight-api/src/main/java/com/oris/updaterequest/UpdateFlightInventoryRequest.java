package com.oris.updaterequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateFlightInventoryRequest(
        @NotNull
        @PositiveOrZero
        BigDecimal price
) {
}
