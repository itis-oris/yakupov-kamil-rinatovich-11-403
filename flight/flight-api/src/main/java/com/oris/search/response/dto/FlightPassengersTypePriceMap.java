package com.oris.search.response.dto;

import com.oris.common.PassengerType;

import java.util.Map;

public record FlightPassengersTypePriceMap(
        Map<PassengerType, FlightPassengersTypePrice> prices
) {
}
