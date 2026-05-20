package com.arsaka.referencedata.exception;

import com.arsaka.exception.NotFoundException;

public class AirplaneNotFoundException extends NotFoundException {
    public AirplaneNotFoundException() {
        super("Airplane not found");
    }
}
