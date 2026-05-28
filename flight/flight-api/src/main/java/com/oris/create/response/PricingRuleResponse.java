package com.oris.create.response;

import com.oris.common.PassengerType;

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
