package com.oris.search.response.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SeatType {
    WINDOW("window"),
    MIDDLE("middle"),
    AISLE("aisle");

    private final String value;

    SeatType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
