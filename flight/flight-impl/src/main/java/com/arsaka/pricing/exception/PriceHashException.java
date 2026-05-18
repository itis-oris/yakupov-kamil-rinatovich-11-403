package com.arsaka.pricing.exception;

public class PriceHashException extends RuntimeException {
    public PriceHashException(String message) {
        super("Failed to compute price hash | message=%s".formatted(message));
    }
}
