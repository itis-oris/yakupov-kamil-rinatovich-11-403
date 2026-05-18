package com.arsaka.search.request;

import com.arsaka.common.PassengerType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FlightRequest(
        @NotNull
        UUID seatId,

        @NotNull
        UUID fareId,

        @NotNull
        PassengerType passengerType
) {
}
