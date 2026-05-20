package com.arsaka.referencedata.exception;

import com.arsaka.exception.NotFoundException;

public class SeatNotFoundException extends NotFoundException {
    public SeatNotFoundException() {
        super("Seat not found");
    }
}
