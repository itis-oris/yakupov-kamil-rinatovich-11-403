package com.arsaka.referencedata.exception;

import com.arsaka.exception.NotFoundException;

public class AirplaneTypeNotFoundException extends NotFoundException {
    public AirplaneTypeNotFoundException() {
        super("Airplane type not found");
    }
}
