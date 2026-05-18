package com.arsaka.pricing.dto;

import com.arsaka.common.PricingAdjustmentType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PricingAdjRecord {
    private PricingAdjustmentType type;
    private BigDecimal value;
}
