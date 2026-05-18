package com.arsaka.pricing.exception;

import com.arsaka.exception.NotFoundException;

import java.util.UUID;

public class FareNotFoundException extends NotFoundException {
    public FareNotFoundException(UUID fareId) {
        super("Fare not found | fare id=%s".formatted(fareId));
    }
}
