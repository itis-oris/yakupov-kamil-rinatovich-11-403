package com.arsaka.pricing.exception;

import com.arsaka.exception.NotFoundException;

public class PricingAdjustmentNotFoundException extends NotFoundException {
    public PricingAdjustmentNotFoundException() {
        super("Pricing adjustment not found");
    }
}
