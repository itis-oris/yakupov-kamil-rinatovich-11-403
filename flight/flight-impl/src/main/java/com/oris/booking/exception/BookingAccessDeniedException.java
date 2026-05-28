package com.oris.booking.exception;

import com.oris.exception.AccessDeniedException;

public class BookingAccessDeniedException extends AccessDeniedException {
    public BookingAccessDeniedException() {
        super("Access denied to booking");
    }
}
