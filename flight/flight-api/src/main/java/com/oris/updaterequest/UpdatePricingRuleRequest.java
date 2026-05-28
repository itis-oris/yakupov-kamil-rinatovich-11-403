package com.oris.updaterequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdatePricingRuleRequest(
        @NotNull
        @Positive
        BigDecimal multiplier
) {
}
