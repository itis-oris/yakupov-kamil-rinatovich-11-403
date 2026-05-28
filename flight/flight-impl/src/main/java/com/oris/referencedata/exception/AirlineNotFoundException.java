package com.oris.referencedata.exception;

import com.oris.exception.NotFoundException;

public class AirlineNotFoundException extends NotFoundException {
    public AirlineNotFoundException() {
        super("Airline not found");
    }
}
