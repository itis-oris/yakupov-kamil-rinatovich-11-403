package com.oris.search.response.dto;

import com.oris.common.PassengerType;

import java.math.BigDecimal;

public record FlightPassengerTypePrice(
        PassengerType passengerType,
        BigDecimal priceForOnePassenger,
        String priceHash
) {
}
