package com.oris.pricing.exception;

import com.oris.exception.NotFoundException;

public class PriceNotFoundException extends NotFoundException {
    public PriceNotFoundException() {
        super("Price not found");
    }
}
