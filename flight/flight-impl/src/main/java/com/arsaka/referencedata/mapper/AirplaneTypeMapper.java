package com.arsaka.referencedata.mapper;

import com.arsaka.flight.dto.FlightRecord;
import com.arsaka.search.response.dto.FlightsSearchAirplaneType;
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