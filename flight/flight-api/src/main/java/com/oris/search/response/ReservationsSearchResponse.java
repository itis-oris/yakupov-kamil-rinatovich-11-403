package com.oris.search.response;

import com.oris.search.response.dto.TicketSearch;

import java.util.List;

public record ReservationsSearchResponse(
        List<TicketSearch> tickets,
        String nextCursor
) {
}
