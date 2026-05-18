package com.arsaka.search.response.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SeatReservedStatus {
    AVAILABLE("available"),
    HELD("held"),
    SOLD("sold"),
    BLOCKED("blocked");

    private final String value;

    SeatReservedStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
