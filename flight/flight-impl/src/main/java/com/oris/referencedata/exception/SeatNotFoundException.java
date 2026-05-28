package com.oris.referencedata.exception;

import com.oris.exception.NotFoundException;

public class SeatNotFoundException extends NotFoundException {
    public SeatNotFoundException() {
        super("Seat not found");
    }
}
