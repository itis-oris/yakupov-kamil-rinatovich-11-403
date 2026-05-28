package com.oris.flight.repository;

import com.oris.flight.dto.FlightSearchDto;
import com.oris.search.response.dto.FlightsSearchAirplaneType;
import com.oris.search.response.dto.FlightsSearchAirline;
import com.oris.search.response.dto.FlightsSearchAirport;
import com.oris.search.response.dto.FlightsSearchFilter;
import com.oris.flight.util.FlightConditionBuilder;
import com.oris.jooq.tables.*;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

import static com.oris.jooq.Tables.*;
import static org.jooq.impl.DSL.*;


@Repository
@RequiredArgsConstructor
public class FlightSearchFilterRepository {

    private final DSLContext dsl;

    private final static Flight flight = FLIGHT.as("flight");
    private final static Route route = ROUTE.as("route");
    private final static Airport depAir = AIRPORT.as("depAir");
    private final static Country depCountry = COUNTRY.as("depCountry");
    private final static City depCity = CITY.as("depCity");
    private final static Airport arrAir = AIRPORT.as("arrAir");
    private final static Country arrCountry = COUNTRY.as("arrCountry");
    private final static City arrCity = CITY.as("arrCity");
    private final static FlightInventory fInv = FLIGHT_INVENTORY.as("fInv");
    private final static Airline airline = AIRLINE.as("airline");
    private final static AirplaneType airplaneType = AIRPLANE_TYPE.as("airplaneType");

    public FlightsSearchFilter findFilter(FlightSearchDto dto) {

        List<Condition> conditions = FlightConditionBuilder.buildFlightSearchFilterConditions(
                dto,
                flight,
                depAir,
                arrAir,
                fInv
        );

        var departureAirports = multisetAgg(depAir.CODE, depAir.NAME, depAir.CITY_CODE, depCountry.NAME)
                .as("departureAirports")
                .convertFrom(r -> r == null
                        ? new HashSet<FlightsSearchAirport>()
                        : new HashSet<>(r.map(rec -> new FlightsSearchAirport(
                        rec.value1(), rec.value2(), rec.value3(), rec.value4()
                ))));

        var arrivalAirports = multisetAgg(arrAir.CODE, arrAir.NAME, arrAir.CITY_CODE, arrCountry.NAME)
                .as("arrivalAirports")
                .convertFrom(r -> r == null
                        ? new HashSet<FlightsSearchAirport>()
                        : new HashSet<>(r.map(rec -> new FlightsSearchAirport(
                        rec.value1(), rec.value2(), rec.value3(), rec.value4()
                ))));

        var airlines = multisetAgg(airline.CODE, airline.NAME)
                .as("airlines")
                .convertFrom(r -> r == null
                        ? new HashSet<FlightsSearchAirline>()
                        : new HashSet<>(r.map(rec -> new FlightsSearchAirline(
                        rec.value1(), rec.value2()
                ))));

        var airplaneTypes = multisetAgg(airplaneType.CODE, airplaneType.MANUFACTURER, airplaneType.MODEL)
                .as("airplaneTypes")
                .convertFrom(r -> r == null
                        ? new HashSet<FlightsSearchAirplaneType>()
                        : new HashSet<>(r.map(rec -> new FlightsSearchAirplaneType(
                        rec.value1(), rec.value2(), rec.value3()
                ))));

        var priceMin = min(fInv.PRICE).as("priceBegin");
        var priceMax = max(fInv.PRICE).as("priceEnd");

        return dsl
                .select(
                        departureAirports,
                        arrivalAirports,
                        airlines,
                        airplaneTypes,
                        priceMin,
                        priceMax
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
                .join(airline).on(route.AIRLINE_CODE.eq(airline.CODE))
                .join(airplaneType).on(flight.AIRPLANE_TYPE_CODE.eq(airplaneType.CODE))
                .where(conditions)
                .fetchOne(r -> new FlightsSearchFilter(
                        r.get(airlines),
                        r.get(departureAirports),
                        r.get(arrivalAirports),
                        r.get(airplaneTypes),
                        r.get(priceMin),
                        r.get(priceMax)
                ));
    }
}