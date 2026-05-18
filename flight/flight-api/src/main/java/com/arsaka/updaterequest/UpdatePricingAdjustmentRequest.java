package com.arsaka.updaterequest;

import com.arsaka.common.PricingAdjustmentType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdatePricingAdjustmentRequest(
        @NotNull
        PricingAdjustmentType type,

        @NotNull
        BigDecimal value
) {
}
