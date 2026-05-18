package com.arsaka.referencedata.exception;

import com.arsaka.common.CabinClass;
import com.arsaka.exception.BadRequestException;

import java.util.UUID;

public class SeatCabinClassException extends BadRequestException {
    public SeatCabinClassException(
            UUID seatId,
            UUID fareId,
            CabinClass seatCabinClass,
            CabinClass fareCabinClass
    ) {
        super("Seat cabin class doesn't match with fare cabin class | seatId=%s | fareId=%s | seatCabinClass=%s | fareCabinClass=%s"
                .formatted(
                        seatId,
                        fareId,
                        seatCabinClass,
                        fareCabinClass
        ));
    }
}
