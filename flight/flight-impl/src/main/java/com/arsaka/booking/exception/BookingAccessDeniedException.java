package com.arsaka.booking.exception;

import com.arsaka.exception.AccessDeniedException;

public class BookingAccessDeniedException extends AccessDeniedException {
    public BookingAccessDeniedException() {
        super("Access denied to booking");
    }
}
