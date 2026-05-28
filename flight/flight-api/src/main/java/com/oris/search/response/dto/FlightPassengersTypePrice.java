package com.oris.search.response.dto;

import java.math.BigDecimal;

public record FlightPassengersTypePrice(
        int passengersCount,
        BigDecimal priceForOnePassenger,
        String priceHash
) {
}
