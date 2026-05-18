package com.arsaka.pricing.exception;

import com.arsaka.exception.NotFoundException;

import java.util.UUID;

public class PricingAdjustmentNotFoundException extends NotFoundException {
    public PricingAdjustmentNotFoundException(UUID id) {
        super("Pricing adjustment not found | id=%s".formatted(id));
    }
}
