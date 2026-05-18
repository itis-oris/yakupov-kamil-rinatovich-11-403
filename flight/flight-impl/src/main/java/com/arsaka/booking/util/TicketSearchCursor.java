package com.arsaka.booking.util;

import java.time.Instant;
import java.util.UUID;

public record TicketSearchCursor(
        Instant primaryValue,
        UUID ticketId
) {
}
