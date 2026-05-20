package com.arsaka.booking.exception;

import com.arsaka.exception.AccessDeniedException;

public class TicketAccessDeniedException extends AccessDeniedException {
    public TicketAccessDeniedException() {
        super("Access denied to ticket");
    }
}
