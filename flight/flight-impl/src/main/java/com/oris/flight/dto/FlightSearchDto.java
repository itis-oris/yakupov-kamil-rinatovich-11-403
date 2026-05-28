package com.oris.flight.dto;

import com.oris.common.CabinClass;
import com.oris.search.request.dto.OrderType;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightSearchDto {
    private String cityCodeFrom;
    private String cityCodeTo;
    private Instant departureStart;
    private Instant departureEnd;
    private Instant arrivalStart;
    private Instant arrivalEnd;
    private Boolean isArrivalInclude;
    private CabinClass cabinClass;
    private Integer passengersCount;
    private OrderType orderType;
}

