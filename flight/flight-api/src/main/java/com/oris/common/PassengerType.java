package com.oris.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PassengerType {
    ADULT("adult"),
    CHILD("child"),
    INFANT("infant");

    private final String value;

    PassengerType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static PassengerType fromValue(String value) {
        for (PassengerType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown PassengerType: %s".formatted(value));
    }
}
