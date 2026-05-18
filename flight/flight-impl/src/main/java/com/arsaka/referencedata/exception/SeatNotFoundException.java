package com.arsaka.referencedata.exception;

import com.arsaka.exception.NotFoundException;

import java.util.UUID;

public class SeatNotFoundException extends NotFoundException {
    public SeatNotFoundException(UUID seatId) {
        super("Seat not found | seatId=%s".formatted(seatId));
    }
}
