package com.arsaka.referencedata.exception;

import com.arsaka.exception.ConflictException;

public class AirlineAlreadyExistsException extends ConflictException {
    public AirlineAlreadyExistsException(String number) {
        super("Airline with this code already exists | airline code=%s".formatted(number));
    }
}
