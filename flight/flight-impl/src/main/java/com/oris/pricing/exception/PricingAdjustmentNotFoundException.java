package com.oris.pricing.exception;

import com.oris.exception.NotFoundException;

public class PricingAdjustmentNotFoundException extends NotFoundException {
    public PricingAdjustmentNotFoundException() {
        super("Pricing adjustment not found");
    }
}
