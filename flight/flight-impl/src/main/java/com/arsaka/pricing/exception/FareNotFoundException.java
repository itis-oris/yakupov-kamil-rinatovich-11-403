package com.arsaka.pricing.exception;

import com.arsaka.exception.NotFoundException;

public class FareNotFoundException extends NotFoundException {
    public FareNotFoundException() {
        super("Fare not found");
    }
}
