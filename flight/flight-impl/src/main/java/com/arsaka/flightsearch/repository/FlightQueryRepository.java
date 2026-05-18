package com.arsaka.flightsearch.repository;

import com.arsaka.common.CabinClass;
import com.arsaka.flightsearch.dto.FlightRecord;
import com.arsaka.flightsearch.dto.FlightSearchDto;
import com.arsaka.flightsearch.dto.FlightSearchRecord;
import com.arsaka.search.request.dto.FlightsSearchFilter;
import com.arsaka.flightsearch.util.FlightConditionBuilder;
import com.arsaka.flightsearch.util.FlightSearchCursor;
import com.arsaka.flightsearch.util.FlightOrderBuilder;
import com.arsaka.jooq.tables.*;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

import static com.arsaka.jooq.Tables.*;


@Repository
@RequiredArgsConstructor
public class FlightQueryRepository {

    private final DSLContext dsl;

    private final static Flight flight = FLIGHT.as("flight");
    private final static Airline airline = AIRLINE.as("airline");
    private final static AirplaneType airplaneType = AIRPLANE_TYPE.as("airplaneType");
    private final static Route route = ROUTE.as("route");
    private final static Airport depAir = AIRPORT.as("depAir");
    private final static Country depCountry = COUNTRY.as("depCountry");
    private final static City depCity = CITY.as("depCity");
    private final static Airport arrAir = AIRPORT.as("arrAir");
    private final static Country arrCountry = COUNTRY.as("arrCountry");
    private final static City arrCity = CITY.as("arrCity");
    private final static FlightInventory fInv = FLIGHT_INVENTORY.as("fInv");
    private final static Fare fare = FARE.as("fare");

    public List<FlightSearchRecord> findFlights(
            FlightSearchDto dto,
            FlightsSearchFilter filterRequest,
            FlightSearchCursor cursor,
            int limit
    ) {

        List<Condition> conditions = FlightConditionBuilder.buildFlightsConditions(
                dto,
                filterRequest,
                cursor,
                flight,
                route,
                depAir,
                arrAir,
                fInv,
                fare
        );

        List<SortField<?>> orderBy = FlightOrderBuilder.build(dto.getOrderType(), flight, fInv);

        return dsl
                .select(
                        flight.ID.as("flightId"),
                        depAir.CODE.as("departureAirportCode"),
                        depAir.NAME.as("departureAirportName"),
                        depCity.NAME.as("departureAirportCityName"),
                        depCountry.NAME.as("departureAirportCountryName"),
                        arrAir.CODE.as("arrivalAirportCode"),
                        arrAir.NAME.as("arrivalAirportName"),
                        arrCity.NAME.as("arrivalAirportCityName"),
                        arrCountry.NAME.as("arrivalAirportCountryName"),
                        flight.SCHEDULED_DEPARTURE.as("scheduledDeparture"),
                        flight.SCHEDULED_ARRIVAL.as("scheduledArrival"),
                        fInv.TOTAL_SEATS.as("totalSeats"),
                        fInv.AVAILABLE_SEATS.as("availableSeats"),
                        fInv.HELD_SEATS.as("heldSeats"),
                        fInv.PRICE.as("price")
                )
                .from(flight)
                .join(route).on(flight.ROUTE_ID.eq(route.ID))
                .join(depAir).on(route.DEPARTURE_AIRPORT_CODE.eq(depAir.CODE))
                .join(arrAir).on(route.ARRIVAL_AIRPORT_CODE.eq(arrAir.CODE))
                .join(depCity).on(depAir.CITY_CODE.eq(depCity.CODE))
                .join(depCountry).on(depCity.COUNTRY_CODE.eq(depCountry.CODE))
                .join(arrCity).on(arrAir.CITY_CODE.eq(arrCity.CODE))
                .join(arrCountry).on(arrCity.COUNTRY_CODE.eq(arrCountry.CODE))
                .join(fInv).on(fInv.FLIGHT_ID.eq(flight.ID))
                .where(conditions)
                .orderBy(orderBy)
                .limit(limit)
                .fetchInto(FlightSearchRecord.class);
    }

    public FlightRecord findFlight(UUID flightId, CabinClass cabinClass) {
        List<Condition> conditions = FlightConditionBuilder.buildFlightConditions(flightId, cabinClass, flight, fInv);

        return dsl
                .select(
                        flight.ID.as("flightId"),
                        airline.CODE.as("airlineCode"),
                        airline.NAME.as("airlineName"),
                        depAir.CODE.as("departureAirportCode"),
                        depAir.NAME.as("departureAirportName"),
                        depCity.NAME.as("departureAirportCityName"),
                        depCountry.NAME.as("departureAirportCountryName"),
                        arrAir.CODE.as("arrivalAirportCode"),
                        arrAir.NAME.as("arrivalAirportName"),
                        arrCity.NAME.as("arrivalAirportCityName"),
                        arrCountry.NAME.as("arrivalAirportCountryName"),
                        airplaneType.CODE.as("airplaneTypeCode"),
                        airplaneType.MANUFACTURER.as("airplaneTypeManufacturer"),
                        airplaneType.MODEL.as("airplaneTypeModel"),
                        flight.SCHEDULED_DEPARTURE.as("scheduledDeparture"),
                        flight.SCHEDULED_ARRIVAL.as("scheduledArrival"),
                        fInv.CABIN_CLASS.as("cabinClass"),
                        fInv.TOTAL_SEATS.as("totalSeats"),
                        fInv.AVAILABLE_SEATS.as("availableSeats"),
                        fInv.HELD_SEATS.as("heldSeats"),
                        fInv.PRICE.as("price")
                )
                .from(flight)
                .join(route).on(flight.ROUTE_ID.eq(route.ID))
                .join(airline).on(route.AIRLINE_CODE.eq(airline.CODE))
                .join(depAir).on(route.DEPARTURE_AIRPORT_CODE.eq(depAir.CODE))
                .join(arrAir).on(route.ARRIVAL_AIRPORT_CODE.eq(arrAir.CODE))
                .join(depCity).on(depAir.CITY_CODE.eq(depCity.CODE))
                .join(depCountry).on(depCity.COUNTRY_CODE.eq(depCountry.CODE))
                .join(arrCity).on(arrAir.CITY_CODE.eq(arrCity.CODE))
                .join(arrCountry).on(arrCity.COUNTRY_CODE.eq(arrCountry.CODE))
                .join(airplaneType).on(flight.AIRPLANE_TYPE_CODE.eq(airplaneType.CODE))
                .join(fInv).on(fInv.FLIGHT_ID.eq(flight.ID))
                .where(conditions)
                .fetchOneInto(FlightRecord.class);
    }

}