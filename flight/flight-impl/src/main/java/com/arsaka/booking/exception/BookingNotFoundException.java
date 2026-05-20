package com.arsaka.booking.exception;

import com.arsaka.exception.NotFoundException;

import java.util.UUID;

public class BookingNotFoundException extends NotFoundException {
    public BookingNotFoundException() {
        super("Booking not found");
    }
}
