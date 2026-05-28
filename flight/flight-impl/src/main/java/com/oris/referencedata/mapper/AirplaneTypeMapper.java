package com.oris.referencedata.mapper;

import com.oris.flight.dto.FlightRecord;
import com.oris.search.response.dto.FlightsSearchAirplaneType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AirplaneTypeMapper {

    @Mapping(target = "code", source = "airplaneTypeCode")
    @Mapping(target = "manufacturer", source = "airplaneTypeManufacturer")
    @Mapping(target = "model", source = "airplaneTypeModel")
    FlightsSearchAirplaneType map(FlightRecord record);

//    AirplaneType toEntity(CreateAirplaneTypeRequest request);
//
//    AirplaneTypeResponse toResponse(AirplaneType airplaneType);
}