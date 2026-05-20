package com.arsaka.referencedata.exception;

import com.arsaka.exception.NotFoundException;

public class AirlineNotFoundException extends NotFoundException {
    public AirlineNotFoundException() {
        super("Airline not found");
    }
}
