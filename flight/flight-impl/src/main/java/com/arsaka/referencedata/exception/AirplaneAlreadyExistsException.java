package com.arsaka.referencedata.exception;

import com.arsaka.exception.ConflictException;

public class AirplaneAlreadyExistsException extends ConflictException {
    public AirplaneAlreadyExistsException(String number) {
        super("Airplane with this number already exists | airplane number=%s".formatted(number));
    }
}
