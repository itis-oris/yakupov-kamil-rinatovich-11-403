package com.oris.booking.exception;

import com.oris.exception.NotFoundException;

public class TicketNotFoundException extends NotFoundException {
    public TicketNotFoundException() {
        super("Ticket not found");
    }

}
