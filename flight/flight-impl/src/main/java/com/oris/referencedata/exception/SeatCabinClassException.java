package com.oris.referencedata.exception;

import com.oris.exception.BadRequestException;

public class SeatCabinClassException extends BadRequestException {
    public SeatCabinClassException() {
        super("Seat cabin class doesn't match with fare cabin class");
    }
}
