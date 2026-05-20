package com.arsaka.pricing.exception;

public class PriceHashException extends RuntimeException {
    public PriceHashException() {
        super("Failed to compute price hash");
    }
}
