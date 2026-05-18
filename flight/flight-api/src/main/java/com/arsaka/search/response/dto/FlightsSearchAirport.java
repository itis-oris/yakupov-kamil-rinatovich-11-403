package com.arsaka.search.response.dto;

public record FlightsSearchAirport(
        String code,
        String name,
        String cityName,
        String countryName
) {
}
