package com.oris.search.request.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderType {
    DEPARTURE_ASC("departure asc"),
    PRICE_ASC("price asc"),
    PRICE_DESC("price desc");

    private final String value;

    OrderType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
