package com.arsaka.referencedata.exception;

import com.arsaka.exception.NotFoundException;

import java.util.UUID;

public class AirplaneNotFoundException extends NotFoundException {
    public AirplaneNotFoundException(UUID id) {
        super("Airplane not found | airline id=%s".formatted(id));
    }
}
