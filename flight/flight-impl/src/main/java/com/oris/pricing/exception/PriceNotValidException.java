package com.oris.pricing.exception;

import com.oris.exception.ConflictException;

public class PriceNotValidException extends ConflictException {
    public PriceNotValidException() {
        super("Price not valid exception");
    }
}
