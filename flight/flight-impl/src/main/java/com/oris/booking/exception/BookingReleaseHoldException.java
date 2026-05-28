package com.oris.booking.exception;

import com.oris.exception.ConflictException;

public class BookingReleaseHoldException extends ConflictException {
    public BookingReleaseHoldException() {
        super("Booking already confirmed");
    }
}
