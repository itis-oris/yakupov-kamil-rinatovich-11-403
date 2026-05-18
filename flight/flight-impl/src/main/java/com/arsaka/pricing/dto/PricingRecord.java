package com.arsaka.pricing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingRecord {
    private PricingAdjRecord adjRecord;
    private PricingRuleRecord ruleRecord;
}
