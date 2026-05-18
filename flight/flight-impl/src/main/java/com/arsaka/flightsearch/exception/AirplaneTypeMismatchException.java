package com.arsaka.flightsearch.exception;

import com.arsaka.exception.ConflictException;

public class AirplaneTypeMismatchException extends ConflictException {
    public AirplaneTypeMismatchException(String assignedAirplaneTypeCode, String requiredAirplaneTypeCode) {
        super("Assigned airplane type does not match flight airplane type | assigned airplane type code=%s | required airplane type code=%s"
                .formatted(assignedAirplaneTypeCode, requiredAirplaneTypeCode));
    }
}
