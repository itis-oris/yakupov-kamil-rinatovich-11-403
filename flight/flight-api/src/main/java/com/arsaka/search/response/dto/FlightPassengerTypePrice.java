package com.arsaka.search.response.dto;

import com.arsaka.common.PassengerType;

import java.math.BigDecimal;

public record FlightPassengerTypePrice(
        PassengerType passengerType,
        BigDecimal priceForOnePassenger,
        String priceHash
) {
}
