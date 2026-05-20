package com.arsaka.pricing.exception;

import com.arsaka.exception.ConflictException;

public class PricingRuleAlreadyExistsException extends ConflictException {
    public PricingRuleAlreadyExistsException() {
        super("Pricing rule already exists");
    }
}
