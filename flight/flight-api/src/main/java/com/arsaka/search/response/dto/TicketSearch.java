package com.arsaka.search.response.dto;

import com.arsaka.common.PassengerType;
import com.arsaka.event.dto.FlightPassenger;
import com.arsaka.search.request.dto.TicketStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TicketSearch(
        UUID id,
        TicketStatus status,
        UUID bookingId,
        FlightPassenger passenger,
        PassengerType passengerType,
        BigDecimal totalPrice,
        Instant createdAt
) {
}
