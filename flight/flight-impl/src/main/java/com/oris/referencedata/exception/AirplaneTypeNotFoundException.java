package com.oris.referencedata.exception;

import com.oris.exception.NotFoundException;

public class AirplaneTypeNotFoundException extends NotFoundException {
    public AirplaneTypeNotFoundException() {
        super("Airplane type not found");
    }
}
