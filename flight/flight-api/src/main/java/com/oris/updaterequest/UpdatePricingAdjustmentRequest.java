package com.oris.updaterequest;

import com.oris.common.PricingAdjustmentType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdatePricingAdjustmentRequest(
        @NotNull
        PricingAdjustmentType type,

        @NotNull
        BigDecimal value
) {
}
