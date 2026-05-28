package com.oris.referencedata.exception;

import com.oris.exception.ConflictException;

public class AirlineAlreadyExistsException extends ConflictException {
    public AirlineAlreadyExistsException() {
        super("Airline with this code already exists");
    }
}
