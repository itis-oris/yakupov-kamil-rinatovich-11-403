package com.oris.referencedata.exception;

import com.oris.exception.ConflictException;

public class SeatHoldException extends ConflictException {
    public SeatHoldException() {
        super("Seat is not available");
    }
}
