package com.oris.search.request;

import com.oris.search.request.dto.TicketStatus;
import jakarta.validation.Valid;

public record ReservationsSearchRequest(
        @Valid
        TicketStatus status,
        String nextCursor
) {
}
