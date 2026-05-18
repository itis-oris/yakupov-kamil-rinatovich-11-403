package com.arsaka.pricing.exception;

import com.arsaka.common.PassengerType;
import com.arsaka.exception.NotFoundException;

import java.util.Set;
import java.util.UUID;

public class PricingRuleNotFoundException extends NotFoundException {
    public PricingRuleNotFoundException(UUID fareId, PassengerType passengerType) {
        super("Pricing rule not found | fareId=%s | passengerType=%s".formatted(fareId, passengerType));
    }

    public PricingRuleNotFoundException(UUID id) {
        super("Pricing rule not found | id=%s".formatted(id));
    }

    public PricingRuleNotFoundException(UUID fareId, Set<PassengerType> passengerTypes) {
        super("Pricing rule not found | fare id=%s | passengers=%s".formatted(fareId, passengerTypes));
    }
}
