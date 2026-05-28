package com.oris.search.request;

import com.oris.common.PassengerType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FlightRequest(
        UUID seatId,

        @NotNull
        UUID fareId,

        @NotNull
        PassengerType passengerType
) {
}
