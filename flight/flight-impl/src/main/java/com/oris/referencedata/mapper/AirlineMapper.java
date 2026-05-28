package com.oris.referencedata.mapper;

import com.oris.create.request.CreateAirlineRequest;
import com.oris.create.response.AirlineResponse;
import com.oris.flight.dto.FlightRecord;
import com.oris.referencedata.model.Airline;
import com.oris.referencedata.model.Country;
import com.oris.search.response.dto.FlightsSearchAirline;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AirlineMapper {

    FlightsSearchAirline toSearchAirline(Airline airline);

    @Mapping(target = "code", source = "airlineCode")
    @Mapping(target = "name", source = "airlineName")
    FlightsSearchAirline map(FlightRecord record);

    default Airline toEntity(CreateAirlineRequest request, Country country) {
        return Airline.builder()
                .code(request.code())
                .name(request.name())
                .country(country)
                .build();
    }

    @Mapping(target = "countryCode", source = "country.code")
    AirlineResponse toResponse(Airline airline);
}