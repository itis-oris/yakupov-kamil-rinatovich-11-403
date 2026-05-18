package com.arsaka.flightsearch.util;

import com.arsaka.common.CabinClass;
import com.arsaka.flightsearch.dto.FlightSearchDto;
import com.arsaka.search.request.dto.FlightsSearchFilter;
import com.arsaka.jooq.tables.*;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.jooq.impl.DSL.row;

public class FlightConditionBuilder {

    public static List<Condition> buildFlightsConditions(
            FlightSearchDto dto,
            FlightsSearchFilter filterRequest,
            FlightSearchCursor cursor,
            Flight flight,
            Route route,
            Airport depAir,
            Airport arrAir,
            FlightInventory fInv,
            Fare fare
    ) {
        List<Condition> conditions = new ArrayList<>();

        baseConditions(conditions, dto, depAir, arrAir, fInv);
        departureConditions(conditions, dto, filterRequest, flight);
        arrivalConditions(conditions, dto, filterRequest, flight);

        if (filterRequest != null) {
            filterConditions(conditions, dto, filterRequest, flight, route, depAir, arrAir, fInv, fare);
        }

        if (cursor != null) {
            cursorConditions(conditions, cursor, flight, fInv);
        }

        return conditions;
    }

    public static List<Condition> buildFlightSearchFilterConditions(
            FlightSearchDto dto,
            Flight flight,
            Airport depAir,
            Airport arrAir,
            FlightInventory fInv
    ) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(depAir.CITY_CODE.eq(dto.getCityCodeFrom()));
        conditions.add(arrAir.CITY_CODE.eq(dto.getCityCodeTo()));

        conditions.add(flight.SCHEDULED_DEPARTURE.greaterOrEqual(dto.getDepartureStart()));
        conditions.add(flight.SCHEDULED_DEPARTURE.lessThan(dto.getDepartureEnd()));

        conditions.add(fInv.AVAILABLE_SEATS.greaterOrEqual(dto.getPassengersCount()));

        conditions.add(fInv.CABIN_CLASS.eq(dto.getCabinClass().name()));

        if (dto.getIsArrivalInclude()) {
            conditions.add(flight.SCHEDULED_ARRIVAL.greaterOrEqual(dto.getArrivalStart()));
            conditions.add(flight.SCHEDULED_ARRIVAL.lessThan(dto.getArrivalEnd()));
        }

        return conditions;
    }

    public static List<Condition> buildFlightConditions(
            UUID flightId,
            CabinClass cabinClass,
            Flight flight,
            FlightInventory fInv
    ) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(flight.ID.eq(flightId));
        conditions.add(fInv.CABIN_CLASS.eq(cabinClass.name()));
        return conditions;
    }

    private static void baseConditions(
            List<Condition> conditions,
            FlightSearchDto dto,
            Airport depAir,
            Airport arrAir,
            FlightInventory fInv
    ) {
        conditions.add(depAir.CITY_CODE.eq(dto.getCityCodeFrom()));
        conditions.add(arrAir.CITY_CODE.eq(dto.getCityCodeTo()));

        conditions.add(fInv.AVAILABLE_SEATS.greaterOrEqual(dto.getPassengersCount()));
        conditions.add(fInv.CABIN_CLASS.eq(dto.getCabinClass().name()));
    }


    private static void departureConditions(
            List<Condition> conditions,
            FlightSearchDto dto,
            FlightsSearchFilter filter,
            Flight flight
    ) {

        if (filter != null && filter.scheduledDepartureTimeType() != null) {
            conditions.add(flight.SCHEDULED_DEPARTURE.greaterOrEqual(
                            dto.getDepartureStart().plus(Duration.ofHours(filter.scheduledDepartureTimeType().getStart()))
                    )
            );
            conditions.add(flight.SCHEDULED_DEPARTURE.lessThan(
                            dto.getDepartureEnd().plus(Duration.ofHours(filter.scheduledDepartureTimeType().getEnd()))
                    )
            );
        } else {
            conditions.add(flight.SCHEDULED_DEPARTURE.greaterOrEqual(dto.getDepartureStart()));
            conditions.add(flight.SCHEDULED_DEPARTURE.lessThan(dto.getDepartureEnd()));
        }
    }

    private static void arrivalConditions(
            List<Condition> conditions,
            FlightSearchDto dto,
            FlightsSearchFilter filter,
            Flight flight
    ) {
        if (filter != null && filter.scheduledArrivalTimeType() != null) {
            if (dto.getIsArrivalInclude()) {
                conditions.add(flight.SCHEDULED_ARRIVAL.greaterOrEqual(
                                dto.getArrivalStart().plus(Duration.ofHours(filter.scheduledArrivalTimeType().getStart()))
                        )
                );
                conditions.add(flight.SCHEDULED_ARRIVAL.lessThan(
                                dto.getArrivalEnd().plus(Duration.ofHours(filter.scheduledArrivalTimeType().getEnd()))
                        )
                );
            }
        } else {
            if (dto.getIsArrivalInclude()) {
                conditions.add(flight.SCHEDULED_ARRIVAL.greaterOrEqual(dto.getArrivalStart()));
                conditions.add(flight.SCHEDULED_ARRIVAL.lessThan(dto.getArrivalEnd()));
            }
        }
    }

    private static void filterConditions(
            List<Condition> conditions,
            FlightSearchDto dto,
            FlightsSearchFilter filter,
            Flight flight,
            Route route,
            Airport depAir,
            Airport arrAir,
            FlightInventory fInv,
            Fare fare
    ) {
        conditions.add(
                DSL.exists(
                        DSL.selectOne()
                                .from(fare)
                                .where(fare.AIRLINE_CODE.eq(route.AIRLINE_CODE)
                                        .and(fare.CABIN_CLASS.eq(dto.getCabinClass().name()))
                                        .and(fare.IS_ACTIVE.isTrue())
                                        .and(filter.isBaggageIncluded() != null
                                                ? fare.BAGGAGE_INCLUDED.eq(filter.isBaggageIncluded())
                                                : DSL.noCondition())
                                        .and(filter.isRefundable() != null
                                                ? fare.IS_REFUNDABLE.eq(filter.isRefundable())
                                                : DSL.noCondition())
                                )
                )
        );

        if (filter.airlineCodes() != null && !filter.airlineCodes().isEmpty()) {
            conditions.add(route.AIRLINE_CODE.in(filter.airlineCodes()));
        }

        if (filter.airportDepartureCodes() != null && !filter.airportDepartureCodes().isEmpty()) {
            conditions.add(depAir.CODE.in(filter.airportDepartureCodes()));
        }

        if (filter.airportArrivalCodes() != null && !filter.airportArrivalCodes().isEmpty()) {
            conditions.add(arrAir.CODE.in(filter.airportArrivalCodes()));
        }

        if (filter.airplaneTypeCodes() != null && !filter.airplaneTypeCodes().isEmpty()) {
            conditions.add(flight.AIRPLANE_TYPE_CODE.in(filter.airplaneTypeCodes()));
        }

        if(filter.priceRange() != null) {
            if (filter.priceRange().begin() != null) {
                conditions.add(fInv.PRICE.greaterOrEqual(filter.priceRange().begin()));
            }

            if (filter.priceRange().end() != null) {
                conditions.add(fInv.PRICE.lessOrEqual(filter.priceRange().end() ));
            }
        }
    }

    private static void cursorConditions(
            List<Condition> conditions,
            FlightSearchCursor cursor,
            Flight flight,
            FlightInventory fInv
    ) {
        switch (cursor.orderType()) {
            case DEPARTURE_ASC -> {
                if (!(cursor.primaryValue() instanceof Instant departureValue)) {
                    throw new IllegalArgumentException(
                            "Cursor primaryValue must be Instant for DEPARTURE_ASC, got: %s"
                                    .formatted(cursor.primaryValue().getClass().getName())
                    );
                }
                conditions.add(
                        row(flight.SCHEDULED_DEPARTURE, flight.ID)
                                .gt(row(departureValue, cursor.flightId()))
                );
            }
            case PRICE_ASC -> {
                if (!(cursor.primaryValue() instanceof BigDecimal priceValue)) {
                    throw new IllegalArgumentException(
                            "Cursor primaryValue must be BigDecimal for PRICE_ASC, got: %s"
                                    .formatted(cursor.primaryValue().getClass().getName())
                    );
                }
                conditions.add(
                        row(fInv.PRICE, fInv.FLIGHT_ID)
                                .gt(row(priceValue, cursor.flightId()))
                );
            }
            case PRICE_DESC -> {
                if (!(cursor.primaryValue() instanceof BigDecimal priceValue)) {
                    throw new IllegalArgumentException(
                            "Cursor primaryValue must be BigDecimal for PRICE_DESC, got: %s"
                                    .formatted(cursor.primaryValue().getClass().getName())
                    );
                }
                conditions.add(
                        row(fInv.PRICE, fInv.FLIGHT_ID)
                                .lt(row(priceValue, cursor.flightId()))
                );
            }
        }
    }

}
