package com.arsaka.create.request;

import com.arsaka.common.PricingAdjustmentType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePricingAdjustmentRequest(
        @NotNull
        UUID flightId,

        @NotNull
        UUID fareId,

        @NotNull
        PricingAdjustmentType type,

        @NotNull
        BigDecimal value
) {
}
