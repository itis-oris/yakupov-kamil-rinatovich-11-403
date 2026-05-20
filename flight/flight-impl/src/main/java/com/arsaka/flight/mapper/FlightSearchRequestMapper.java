package com.arsaka.flight.mapper;

import com.arsaka.flight.dto.FlightSearchDto;
import com.arsaka.search.request.dto.FlightsSearchFilter;
import com.arsaka.search.request.FlightsSearchRequest;
import com.arsaka.search.request.dto.OrderType;
import com.arsaka.common.PassengerType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;

public class FlightSearchRequestMapper {

    private final static OrderType defaultOrderType = OrderType.DEPARTURE_ASC;

    public static FlightSearchDto map(FlightsSearchRequest request) {
        LocalDate departureDate = request.scheduledDate().departure();
        LocalDate arrivalDate = request.scheduledDate().arrival();

        Instant departureStart = departureDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant departureEnd = departureDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        Instant arrivalStart = null;
        Instant arrivalEnd = null;
        boolean isArrivalInclude = false;

        if(arrivalDate != null) {
            arrivalStart = arrivalDate.atStartOfDay(ZoneOffset.UTC).toInstant();
            arrivalEnd = arrivalDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
            isArrivalInclude = true;
        }

        Map<PassengerType, Integer> passengerTypeMap = request.passengers();

        int passengersCount = passengerTypeMap.entrySet().stream()
                .filter(e -> e.getKey() != PassengerType.INFANT)
                .mapToInt(Map.Entry::getValue)
                .sum();

        FlightsSearchFilter filterRequest = request.filter();

        OrderType orderType = defaultOrderType;

        if (filterRequest != null && filterRequest.orderType() != null) {
            orderType = filterRequest.orderType();
        }

        return new FlightSearchDto(
                request.cityCodeFrom(),
                request.cityCodeTo(),
                departureStart,
                departureEnd,
                arrivalStart,
                arrivalEnd,
                isArrivalInclude,
                request.cabinClass(),
                passengersCount,
                orderType
        );
    }
}
