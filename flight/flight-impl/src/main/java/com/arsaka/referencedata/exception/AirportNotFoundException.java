package com.arsaka.referencedata.exception;

import com.arsaka.exception.NotFoundException;

public class AirportNotFoundException extends NotFoundException {
    public AirportNotFoundException(String code) {
        super("Airport not found | airport code=%s".formatted(code));
    }
}
