package com.arsaka.referencedata.exception;

import com.arsaka.exception.ConflictException;

public class SeatHoldException extends ConflictException {
    public SeatHoldException() {
        super("Seat is not available");
    }
}
