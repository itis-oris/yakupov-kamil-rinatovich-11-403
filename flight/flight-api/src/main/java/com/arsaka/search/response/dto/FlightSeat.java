package com.arsaka.search.response.dto;

import com.arsaka.common.CabinClass;

import java.util.UUID;

public record FlightSeat(
        UUID seatId,
        String number,
        CabinClass cabinClass,
        SeatType type,
        boolean hasExtraLegroom,
        boolean isExitRow,
        SeatReservedStatus status
) {
}
