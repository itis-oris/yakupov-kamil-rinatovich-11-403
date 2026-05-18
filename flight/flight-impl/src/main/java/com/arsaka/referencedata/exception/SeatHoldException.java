package com.arsaka.referencedata.exception;

import com.arsaka.exception.ConflictException;

import java.util.UUID;

public class SeatHoldException extends ConflictException {
    public SeatHoldException(UUID flightId, UUID seatId) {
        super("Seat is not available | flightId=%s | seatId=%s".formatted(flightId, seatId));
    }
}
