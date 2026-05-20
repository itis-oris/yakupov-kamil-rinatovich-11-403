package com.arsaka.booking.mapper;

import com.arsaka.common.CabinClass;
import com.arsaka.common.PassengerType;
import com.arsaka.event.FlightsHoldEventRequest;
import com.arsaka.event.dto.FlightHold;
import com.arsaka.flightsearch.service.FlightInventoryService;
import com.arsaka.booking.dto.FlightHoldDto;
import com.arsaka.pricing.service.FareService;
import com.arsaka.referencedata.exception.SeatCabinClassException;
import com.arsaka.referencedata.service.SeatService;
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
