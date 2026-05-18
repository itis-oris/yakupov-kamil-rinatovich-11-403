package com.arsaka.event.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PassengerGender {
    MALE("male"),
    FEMALE("female"),
    OTHER("other");

    private final String value;

    PassengerGender(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
