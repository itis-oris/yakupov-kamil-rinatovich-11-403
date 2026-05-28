package com.oris.booking.exception;

import com.oris.exception.ConflictException;

public class BookingDuplicateException extends ConflictException {
    public BookingDuplicateException() {
        super("Booking already exists");
    }
}
