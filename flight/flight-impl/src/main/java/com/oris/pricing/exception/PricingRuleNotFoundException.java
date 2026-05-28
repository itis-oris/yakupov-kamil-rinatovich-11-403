package com.oris.pricing.exception;

import com.oris.exception.NotFoundException;

public class PricingRuleNotFoundException extends NotFoundException {
    public PricingRuleNotFoundException() {
        super("Pricing rule not found");
    }
}
