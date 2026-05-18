package com.arsaka.pricing.exception;

import com.arsaka.common.PassengerType;
import com.arsaka.exception.NotFoundException;

import java.util.UUID;

public class PriceNotFoundException extends NotFoundException {
    public PriceNotFoundException(UUID flightId, UUID fareId, PassengerType passengerType) {
        super("Price not found | flightId=%s | fareId=%s | passengerType=%s".formatted(flightId, fareId, passengerType));
    }
}
