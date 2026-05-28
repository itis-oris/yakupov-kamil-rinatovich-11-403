package com.oris.create.response;

import com.oris.common.PricingAdjustmentType;

import java.math.BigDecimal;
import java.util.UUID;

public record PricingAdjustmentResponse(
        UUID id,
        UUID flightId,
        UUID fareId,
        PricingAdjustmentType type,
        BigDecimal value,
        boolean active
) {
}
