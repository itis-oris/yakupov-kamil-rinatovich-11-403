package com.arsaka.booking.exception;

import com.arsaka.exception.ConflictException;

import java.util.UUID;

public class BookingDuplicateException extends ConflictException {
    public BookingDuplicateException(UUID bookingId) {
        super("Booking already exists | bookingId=%s".formatted(bookingId));
    }
}
