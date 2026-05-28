package com.oris.pricing.exception;

import com.oris.exception.NotFoundException;

public class FareNotFoundException extends NotFoundException {
    public FareNotFoundException() {
        super("Fare not found");
    }
}
