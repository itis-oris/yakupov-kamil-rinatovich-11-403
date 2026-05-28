package com.oris.search.response;

import com.oris.search.response.dto.FlightFare;
import com.oris.search.response.dto.FlightSearch;
import com.oris.search.response.dto.FlightSeat;
import com.oris.search.response.dto.TicketSearch;

import java.math.BigDecimal;

public record ReservationSearchResponse(
        TicketSearch ticket,
        FlightSearch flight,
        FlightFare fare,
        FlightSeat seat,
        BigDecimal totalPrice
) {
}
