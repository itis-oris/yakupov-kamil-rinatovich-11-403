package com.oris.booking.mapper;

import com.oris.common.CabinClass;
import com.oris.event.dto.FlightHold;
import com.oris.booking.dto.FlightHoldDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FlightReserveMapper {
    FlightHoldDto map(FlightHold flight, CabinClass seatCabinClass, CabinClass fareCabinClass, BigDecimal basePrice);
}