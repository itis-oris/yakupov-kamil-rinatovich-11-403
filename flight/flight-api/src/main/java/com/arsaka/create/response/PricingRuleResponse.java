package com.arsaka.create.response;

import com.arsaka.common.PassengerType;

import java.math.BigDecimal;
import java.util.UUID;

public record PricingRuleResponse(
        UUID id,
        UUID fareId,
        PassengerType passengerType,
        BigDecimal multiplier,
        boolean active
) {
}
