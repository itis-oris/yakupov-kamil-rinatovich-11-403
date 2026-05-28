package com.oris.pricing.exception;

import com.oris.exception.ConflictException;

public class PricingRuleAlreadyExistsException extends ConflictException {
    public PricingRuleAlreadyExistsException() {
        super("Pricing rule already exists");
    }
}
