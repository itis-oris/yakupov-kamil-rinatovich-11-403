package com.arsaka.search.response.dto;

import com.arsaka.common.PassengerType;

import java.util.Map;

public record FlightPassengersTypePriceMap(
        Map<PassengerType, FlightPassengersTypePrice> prices
) {
}
