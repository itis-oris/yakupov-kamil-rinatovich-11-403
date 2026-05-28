package com.oris.search.response.dto;

import com.oris.common.PassengerType;
import com.oris.event.dto.FlightPassenger;
import com.oris.search.request.dto.TicketStatus;

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
