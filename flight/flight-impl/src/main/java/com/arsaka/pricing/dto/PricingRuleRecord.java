package com.arsaka.pricing.dto;

import com.arsaka.common.PassengerType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PricingRuleRecord {
    private PassengerType passengerType;
    private BigDecimal multiplier;
}
