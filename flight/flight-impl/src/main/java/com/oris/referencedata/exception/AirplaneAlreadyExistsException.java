package com.oris.referencedata.exception;

import com.oris.exception.ConflictException;

public class AirplaneAlreadyExistsException extends ConflictException {
    public AirplaneAlreadyExistsException() {
        super("Airplane with this number already exists");
    }
}
