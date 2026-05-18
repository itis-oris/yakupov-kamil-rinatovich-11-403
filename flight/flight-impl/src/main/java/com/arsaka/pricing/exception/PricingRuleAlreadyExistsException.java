package com.arsaka.pricing.exception;

import com.arsaka.common.PassengerType;
import com.arsaka.exception.ConflictException;

import java.util.UUID;

public class PricingRuleAlreadyExistsException extends ConflictException {
    public PricingRuleAlreadyExistsException(UUID fareId, PassengerType passengerType) {
        super("Pricing rule already exists | fare id=%s | passenger type=%s".formatted(fareId, passengerType));
    }
}
