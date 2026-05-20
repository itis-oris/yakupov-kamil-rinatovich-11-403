package com.arsaka.pricing.exception;

import com.arsaka.exception.ConflictException;

public class PriceNotValidException extends ConflictException {
    public PriceNotValidException() {
        super("Price not valid exception");
    }
}
