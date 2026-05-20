package com.arsaka.pricing.exception;

import com.arsaka.exception.NotFoundException;

public class PriceNotFoundException extends NotFoundException {
    public PriceNotFoundException() {
        super("Price not found");
    }
}
