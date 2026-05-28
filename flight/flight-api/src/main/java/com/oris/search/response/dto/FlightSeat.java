package com.oris.search.response.dto;

import com.oris.common.CabinClass;

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
