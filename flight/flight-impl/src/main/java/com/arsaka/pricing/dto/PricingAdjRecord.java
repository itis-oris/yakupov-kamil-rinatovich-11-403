package com.arsaka.pricing.dto;

import com.arsaka.common.PricingAdjustmentType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PricingAdjRecord {
    private PricingAdjustmentType type;
    private BigDecimal value;
}
