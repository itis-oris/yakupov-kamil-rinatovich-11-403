package com.oris.booking.exception;

import com.oris.exception.NotFoundException;

public class BookingNotFoundException extends NotFoundException {
    public BookingNotFoundException() {
        super("Booking not found");
    }
}
