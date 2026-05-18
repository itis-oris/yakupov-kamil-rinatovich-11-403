package com.arsaka.referencedata.exception;

import com.arsaka.exception.NotFoundException;

public class AirplaneTypeNotFoundException extends NotFoundException {
    public AirplaneTypeNotFoundException(String code) {
        super("Airplane type not found | airline type code=%s".formatted(code));
    }
}
