package com.arsaka.referencedata.exception;

import com.arsaka.exception.ConflictException;

public class AirplaneTypeAlreadyExistsException extends ConflictException {
    public AirplaneTypeAlreadyExistsException(String number) {
        super("Airplane type with this code already exists | airplane type code=%s".formatted(number));
    }
}
