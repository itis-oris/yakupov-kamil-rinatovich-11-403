package com.arsaka.booking.exception;

import com.arsaka.exception.NotFoundException;

public class TicketNotFoundException extends NotFoundException {
    public TicketNotFoundException() {
        super("Ticket not found");
    }

}
