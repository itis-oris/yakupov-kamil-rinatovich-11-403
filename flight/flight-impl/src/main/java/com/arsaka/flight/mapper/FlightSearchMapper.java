package com.arsaka.flight.mapper;

import com.arsaka.search.response.dto.FlightSearch;
import com.arsaka.flight.dto.FlightSearchRecord;
import com.arsaka.search.response.dto.FlightsSearchAirport;
import org.mapstruct.*;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FlightSearchMapper {

    @Mapping(target = "departureAirport", expression = "java(mapDepartureAirport(searchRecord))")
    @Mapping(target = "arrivalAirport", expression = "java(mapArrivalAirport(searchRecord))")
    FlightSearch toResponse(FlightSearchRecord searchRecord);

    List<FlightSearch> toResponseList(List<FlightSearchRecord> records);

    default String map(Instant value) {
        return value == null ? null : value.toString();
    }

    default FlightsSearchAirport mapDepartureAirport(FlightSearchRecord record) {
        return new FlightsSearchAirport(
                record.getDepartureAirportCode(),
                record.getDepartureAirportName(),
                record.getDepartureAirportCityName(),
                record.getDepartureAirportCountryName()
        );
    }

    default FlightsSearchAirport mapArrivalAirport(FlightSearchRecord record) {
        return new FlightsSearchAirport(
                record.getArrivalAirportCode(),
                record.getArrivalAirportName(),
                record.getArrivalAirportCityName(),
                record.getArrivalAirportCountryName()
        );
    }
}
