package com.oris.flight.mapper;

import com.oris.create.request.CreateFlightRequest;
import com.oris.create.request.dto.Time;
import com.oris.create.response.FlightResponse;
import com.oris.flight.dto.FlightRecord;
import com.oris.flight.model.Flight;
import com.oris.flight.model.Route;
import com.oris.referencedata.mapper.AirlineMapper;
import com.oris.referencedata.mapper.AirplaneTypeMapper;
import com.oris.referencedata.mapper.AirportMapper;
import com.oris.referencedata.model.Airplane;
import com.oris.referencedata.model.AirplaneType;
import com.oris.search.response.FlightSearchResponse;
import com.oris.search.response.FlightWithFaresResponse;
import com.oris.search.response.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {AirlineMapper.class, AirportMapper.class, AirplaneTypeMapper.class}
)
public interface FlightMapper {

    @Mapping(target = "airline", source = "flightRecord")
    @Mapping(target = "departureAirport", expression = "java(mapDepartureAirport(flightRecord))")
    @Mapping(target = "arrivalAirport", expression = "java(mapArrivalAirport(flightRecord))")
    @Mapping(target = "airplaneType", source = "flightRecord")
    FlightWithFaresResponse map(FlightRecord flightRecord, Set<FlightSeat> seats, Map<UUID, FlightPassengersTypePriceMap> farePrice, Set<FlightFare> fares);

    @Mapping(target = "airline", source = "flightRecord")
    @Mapping(target = "departureAirport", expression = "java(mapDepartureAirport(flightRecord))")
    @Mapping(target = "arrivalAirport", expression = "java(mapArrivalAirport(flightRecord))")
    @Mapping(target = "airplaneType", source = "flightRecord")
    FlightSearchResponse map(FlightRecord flightRecord, FlightSeat seat, FlightFare fare, FlightPassengerTypePrice farePrice);


    default Flight toEntity(CreateFlightRequest request, Route route, AirplaneType airplaneType, Airplane airplane) {
        return Flight.builder()
                .route(route)
                .airplaneType(airplaneType)
                .airplane(airplane)
                .scheduledDeparture(request.scheduledTime().departure())
                .scheduledArrival(request.scheduledTime().arrival())
                .build();
    }


    @Mapping(target = "routeId", source = "flight.route.id")
    @Mapping(target = "airplaneTypeCode", source = "flight.airplaneType.code")
    @Mapping(target = "airplaneId", source = "flight.airplane.id")
    @Mapping(target = "scheduledTime", expression = "java(mapScheduledTime(flight))")
    @Mapping(target = "actualTime", expression = "java(mapActualTime(flight))")
    FlightResponse toResponse(Flight flight);

    default String map(Instant value) {
        return value == null ? null : value.toString();
    }

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

    default Time mapScheduledTime(Flight flight) {
        return new Time(
                flight.getScheduledDeparture(),
                flight.getScheduledArrival()
        );
    }

    default Time mapActualTime(Flight flight) {
        return new Time(
                flight.getActualDeparture(),
                flight.getActualArrival()
        );
    }
}
