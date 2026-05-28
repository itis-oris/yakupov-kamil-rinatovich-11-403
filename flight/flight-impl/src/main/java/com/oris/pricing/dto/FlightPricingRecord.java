package com.oris.pricing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FlightPricingRecord {
    private PricingAdjRecord adjRecord;
    private Set<PricingRuleRecord> ruleRecord;
}
