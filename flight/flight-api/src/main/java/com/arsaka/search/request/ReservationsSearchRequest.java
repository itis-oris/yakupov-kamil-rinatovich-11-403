package com.arsaka.search.request;

import com.arsaka.search.request.dto.TicketStatus;
import jakarta.validation.Valid;

public record ReservationsSearchRequest(
        @Valid
        TicketStatus status,
        String nextCursor
) {
}
