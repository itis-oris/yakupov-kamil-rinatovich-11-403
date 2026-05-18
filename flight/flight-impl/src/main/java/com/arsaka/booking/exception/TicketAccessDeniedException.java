package com.arsaka.booking.exception;

import com.arsaka.exception.AccessDeniedException;

import java.util.UUID;

public class TicketAccessDeniedException extends AccessDeniedException {
    public TicketAccessDeniedException(UUID accountId, UUID ticketId) {
        super("Access denied to ticket | account id=%s | ticket id=%s"
                .formatted(accountId, ticketId));
    }
}
