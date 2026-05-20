package com.arsaka.referencedata.mapper;

import com.arsaka.flight.dto.FlightRecord;
import com.arsaka.flight.dto.FlightSearchRecord;
import com.arsaka.referencedata.model.Airport;
import com.arsaka.search.response.dto.FlightsSearchAirport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AirportMapper {

    @Mapping(target = "cityName",    source = "city.name")
    @Mapping(target = "countryName", source = "city.country.name")
    FlightsSearchAirport toSearchAirport(Airport airport);

    default FlightsSearchAirport mapDepartureAirport(FlightRecord record) {
        return new FlightsSearchAirport(
                record.getDepartureAirportCode(),
                record.getDepartureAirportName(),
                record.getDepartureAirportCityName(),
                record.getDepartureAirportCountryName()
        );
    }

    default FlightsSearchAirport mapArrivalAirport(FlightRecord record) {
        return new FlightsSearchAirport(
                record.getArrivalAirportCode(),
                record.getArrivalAirportName(),
                record.getArrivalAirportCityName(),
                record.getArrivalAirportCountryName()
        );
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