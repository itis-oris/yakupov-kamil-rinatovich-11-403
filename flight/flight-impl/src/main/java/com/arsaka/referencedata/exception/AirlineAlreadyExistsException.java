package com.arsaka.referencedata.exception;

import com.arsaka.exception.ConflictException;

public class AirlineAlreadyExistsException extends ConflictException {
    public AirlineAlreadyExistsException() {
        super("Airline with this code already exists");
    }
}
