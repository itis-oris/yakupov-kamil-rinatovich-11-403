package com.arsaka.pricing.exception;

import com.arsaka.exception.NotFoundException;

public class PricingRuleNotFoundException extends NotFoundException {
    public PricingRuleNotFoundException() {
        super("Pricing rule not found");
    }
}
