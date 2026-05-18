package com.arsaka.referencedata.exception;

import com.arsaka.exception.NotFoundException;

public class AirlineNotFoundException extends NotFoundException {
    public AirlineNotFoundException(String code) {
        super("Airline not found | airline code=%s".formatted(code));
    }
}
