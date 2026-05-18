package com.arsaka.pricing.exception;

import com.arsaka.common.PassengerType;
import com.arsaka.exception.ConflictException;

import java.util.UUID;

public class PriceNotValidException extends ConflictException {
    public PriceNotValidException(
            UUID flightId,
            UUID seatId,
            UUID fareId,
            PassengerType passengerType
    ) {
        super("Price not valid exception | flightId=%s | seatId=%s | fareId=%s | passengerType = %s".formatted(
                flightId,
                seatId,
                fareId,
                passengerType
        ));
    }
}
