package com.oris.referencedata.mapper;

import com.oris.referencedata.model.Route;
import com.oris.search.response.RouteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {AirlineMapper.class, AirportMapper.class}
)
public interface RouteMapper {

    @Mapping(target = "airline",          source = "airline")
    @Mapping(target = "departureAirport", source = "departureAirport")
    @Mapping(target = "arrivalAirport",   source = "arrivalAirport")
    @Mapping(target = "isActive",         source = "active")
    RouteResponse toResponse(Route route);


}