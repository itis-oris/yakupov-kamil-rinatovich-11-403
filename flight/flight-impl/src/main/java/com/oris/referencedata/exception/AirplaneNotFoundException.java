package com.oris.referencedata.exception;

import com.oris.exception.NotFoundException;

public class AirplaneNotFoundException extends NotFoundException {
    public AirplaneNotFoundException() {
        super("Airplane not found");
    }
}
