package com.oris.pricing.dto;

import com.oris.common.PassengerType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PricingRuleRecord {
    private PassengerType passengerType;
    private BigDecimal multiplier;
}
