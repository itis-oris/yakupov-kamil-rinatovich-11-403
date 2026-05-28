package com.oris.pricing.exception;

import com.oris.exception.ConflictException;

public class PricingAdjustmentAlreadyExistsException extends ConflictException {
    public PricingAdjustmentAlreadyExistsException() {
        super("Pricing adjustment already exists");
    }
}
