package com.arsaka.auth.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountRole {
    USER("user"),
    FLIGHT_MANAGER("flight manager"),
    HOTEL_MANAGER("hotel manager"),
    ADMIN("admin");


    private final String value;

    AccountRole(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
