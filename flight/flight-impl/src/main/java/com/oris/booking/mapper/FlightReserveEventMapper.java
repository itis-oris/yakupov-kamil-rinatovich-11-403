package com.oris.booking.mapper;

import com.oris.common.CabinClass;
import com.oris.common.PassengerType;
import com.oris.event.FlightsHoldEventRequest;
import com.oris.event.dto.FlightHold;
import com.oris.flight.service.FlightInventoryService;
import com.oris.booking.dto.FlightHoldDto;
import com.oris.pricing.service.FareService;
import com.oris.referencedata.exception.SeatCabinClassException;
import com.oris.referencedata.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class FlightReserveEventMapper {

    private final FlightReserveMapper flightReserveMapper;
    private final FareService fareService;
    private final SeatService seatService;
    private final FlightInventoryService flightInventoryService;

    public Set<FlightHoldDto> map(FlightsHoldEventRequest event) {
        Set<FlightHoldDto> result = new HashSet<>();
        for (FlightHold flight : event.flights()) {
            CabinClass fareCabinClass = fareService.getCabinClass(flight.fareId());
            CabinClass seatCabinClass = null;
            if(flight.passengerType() != PassengerType.INFANT) {
                seatCabinClass = seatService.getCabinClass(flight.seatId());

                if (!fareCabinClass.equals(seatCabinClass)) {
                    log.debug("Seat cabin class doesn't match with fare cabin class exception| seatId={} | fareId={} | seatCabinClass={} | fareCabinClass={}",
                            flight.seatId(),
                            flight.fareId(),
                            seatCabinClass,
                            fareCabinClass
                    );
                    throw new SeatCabinClassException();
                }
            }

            BigDecimal basePrice = flightInventoryService.getPrice(flight.flightId(), fareCabinClass);

            result.add(flightReserveMapper.map(flight, seatCabinClass, fareCabinClass, basePrice));
        }
        return result;
    }
}
