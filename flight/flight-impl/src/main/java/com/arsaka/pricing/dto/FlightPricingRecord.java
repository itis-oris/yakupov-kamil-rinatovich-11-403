package com.arsaka.pricing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightPricingRecord {
    private PricingAdjRecord adjRecord;
    private Set<PricingRuleRecord> ruleRecord;
}
