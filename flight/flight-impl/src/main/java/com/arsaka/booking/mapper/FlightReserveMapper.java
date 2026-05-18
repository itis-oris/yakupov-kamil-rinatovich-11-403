package com.arsaka.booking.mapper;

import com.arsaka.common.CabinClass;
import com.arsaka.event.dto.FlightHold;
import com.arsaka.booking.dto.FlightHoldDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FlightReserveMapper {
    FlightHoldDto map(FlightHold flight, CabinClass seatCabinClass, CabinClass fareCabinClass, BigDecimal basePrice);
}