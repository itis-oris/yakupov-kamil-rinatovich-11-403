package com.arsaka.pricing.dto;

import com.arsaka.common.PassengerType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PricingRuleRecord {
    private PassengerType passengerType;
    private BigDecimal multiplier;
}
