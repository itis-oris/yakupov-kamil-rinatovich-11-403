package com.arsaka.create.request;

import com.arsaka.common.PassengerType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePricingRuleRequest(
        @NotNull
        UUID fareId,

        @NotNull
        PassengerType passengerType,

        @NotNull
        @Positive
        BigDecimal multiplier
) {
}
