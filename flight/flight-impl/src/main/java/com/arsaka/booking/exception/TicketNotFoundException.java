package com.arsaka.booking.exception;

import com.arsaka.exception.NotFoundException;

import java.util.UUID;

public class TicketNotFoundException extends NotFoundException {
    public TicketNotFoundException(UUID id) {
        super("Ticket not found | id=%s".formatted(id));
    }

}
