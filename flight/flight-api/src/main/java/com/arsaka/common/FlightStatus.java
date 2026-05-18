package com.arsaka.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FlightStatus {
    SCHEDULED("scheduled"),
    ON_TIME("on time"),
    DELAYED("delayed"),
    BOARDING("boarding"),
    DEPARTED("departed"),
    ARRIVED("arrived"),
    CANCELLED("cancelled"),
    DIVERTED("diverted");

    private final String value;

    FlightStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
