package com.arsaka.referencedata.mapper;

import com.arsaka.create.request.CreateAirplaneRequest;
import com.arsaka.create.response.AirplaneResponse;
import com.arsaka.referencedata.model.Airline;
import com.arsaka.referencedata.model.Airplane;
import com.arsaka.referencedata.model.AirplaneType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AirplaneMapper {

    default Airplane toEntity(CreateAirplaneRequest request, AirplaneType airplaneType, Airline airline) {
        return Airplane.builder()
                .number(request.number())
                .airplaneType(airplaneType)
                .airline(airline)
                .manufacturedYear(request.manufacturedYear())
                .build();
    }

    @Mapping(target = "airplaneTypeCode", source = "airplaneType.code")
    @Mapping(target = "airlineCode", source = "airline.code")
    AirplaneResponse toResponse(Airplane airplane);
}