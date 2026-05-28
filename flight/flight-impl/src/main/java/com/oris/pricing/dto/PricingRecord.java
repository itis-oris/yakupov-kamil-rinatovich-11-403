package com.oris.pricing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PricingRecord {
    private PricingAdjRecord adjRecord;
    private PricingRuleRecord ruleRecord;
}
