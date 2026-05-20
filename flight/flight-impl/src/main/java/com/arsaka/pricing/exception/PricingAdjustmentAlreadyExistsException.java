package com.arsaka.pricing.exception;

import com.arsaka.exception.ConflictException;

public class PricingAdjustmentAlreadyExistsException extends ConflictException {
    public PricingAdjustmentAlreadyExistsException() {
        super("Pricing adjustment already exists");
    }
}
