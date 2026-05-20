package com.arsaka.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CabinClass {
    ECONOMY("economy"),
    BUSINESS("business"),
    FIRST("first");

    private final String value;

    CabinClass(String value) {
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

    public static CabinClass fromValue(String value) {
        for (CabinClass cabinClass : values()) {
            if (cabinClass.value.equalsIgnoreCase(value)) {
                return cabinClass;
            }
        }
        throw new IllegalArgumentException("Unknown Cabin class: %s".formatted(value));
    }
}
