package com.arsaka.booking.exception;

import com.arsaka.exception.NotFoundException;

import java.util.UUID;

public class BookingNotFoundException extends NotFoundException {
    public BookingNotFoundException(UUID id) {
        super("Booking not found | id=%s".formatted(id));
    }
}
