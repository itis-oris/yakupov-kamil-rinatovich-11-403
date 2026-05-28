package com.oris.booking.exception;

import com.oris.exception.AccessDeniedException;

public class TicketAccessDeniedException extends AccessDeniedException {
    public TicketAccessDeniedException() {
        super("Access denied to ticket");
    }
}
