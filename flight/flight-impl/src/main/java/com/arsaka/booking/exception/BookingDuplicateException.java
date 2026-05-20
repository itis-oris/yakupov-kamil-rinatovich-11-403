package com.arsaka.booking.exception;

import com.arsaka.exception.ConflictException;

public class BookingDuplicateException extends ConflictException {
    public BookingDuplicateException() {
        super("Booking already exists");
    }
}
