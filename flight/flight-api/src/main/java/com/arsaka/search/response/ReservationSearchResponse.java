package com.arsaka.search.response;

import com.arsaka.search.response.dto.FlightFare;
import com.arsaka.search.response.dto.FlightSearch;
import com.arsaka.search.response.dto.FlightSeat;
import com.arsaka.search.response.dto.TicketSearch;

import java.math.BigDecimal;

public record ReservationSearchResponse(
        TicketSearch ticket,
        FlightSearch flight,
        FlightFare fare,
        FlightSeat seat,
        BigDecimal totalPrice
) {
}
