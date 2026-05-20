package com.arsaka.referencedata.exception;

import com.arsaka.exception.BadRequestException;

public class SeatCabinClassException extends BadRequestException {
    public SeatCabinClassException() {
        super("Seat cabin class doesn't match with fare cabin class");
    }
}
