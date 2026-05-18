package com.arsaka.booking.exception;

import com.arsaka.exception.ConflictException;

import java.util.UUID;

public class BookingReleaseHoldException extends ConflictException {
    public BookingReleaseHoldException(UUID bookingId) {
        super("Booking already confirmed | bookingId=%s".formatted(bookingId));
    }
}
