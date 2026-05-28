package com.oris.event.dto;

import com.oris.common.PassengerType;
import com.oris.validation.annotation.ValidSeatAssignment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

@ValidSeatAssignment
public record FlightHold(
        @NotNull
        UUID flightId,

        UUID seatId,

        @NotNull
        UUID fareId,

        @NotNull
        @Valid
        FlightPassenger passenger,

        @NotNull
        PassengerType passengerType,

        @NotNull
        @PositiveOrZero
        BigDecimal price,

        @NotBlank
        String priceHash
) {
}
