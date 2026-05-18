package com.arsaka.search.response;

import com.arsaka.search.response.dto.TicketSearch;

import java.util.List;

public record ReservationsSearchResponse(
        List<TicketSearch> tickets,
        String nextCursor
) {
}
