package com.arsaka.referencedata.mapper;

import com.arsaka.create.request.CreateAirlineRequest;
import com.arsaka.create.response.AirlineResponse;
import com.arsaka.flight.dto.FlightRecord;
import com.arsaka.referencedata.model.Airline;
import com.arsaka.referencedata.model.Country;
import com.arsaka.search.response.dto.FlightsSearchAirline;
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