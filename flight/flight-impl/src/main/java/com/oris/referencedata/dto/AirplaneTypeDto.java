package com.oris.referencedata.dto;

public record AirplaneTypeDto(
        String code,
        String manufacturer,
        String model,
        Integer totalSeats
) {
}
