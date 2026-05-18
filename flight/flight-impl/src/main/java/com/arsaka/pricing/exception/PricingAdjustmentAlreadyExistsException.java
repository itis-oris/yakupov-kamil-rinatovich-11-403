package com.arsaka.pricing.exception;

import com.arsaka.exception.ConflictException;

import java.util.UUID;

public class PricingAdjustmentAlreadyExistsException extends ConflictException {
    public PricingAdjustmentAlreadyExistsException(UUID flightId, UUID fareId) {
        super("Pricing adjustment already exists | flight id=%s | fare id=%s".formatted(flightId, fareId));
    }
}
