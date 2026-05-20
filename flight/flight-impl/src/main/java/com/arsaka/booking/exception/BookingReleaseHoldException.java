package com.arsaka.booking.exception;

import com.arsaka.exception.ConflictException;

public class BookingReleaseHoldException extends ConflictException {
    public BookingReleaseHoldException() {
        super("Booking already confirmed");
    }
}
