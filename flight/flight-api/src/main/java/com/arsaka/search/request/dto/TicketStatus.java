package com.arsaka.search.request.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketStatus {
    ISSUED("issued"),
    CANCELLED("cancelled"),
    REFUNDED("refunded"),
    CONFIRMED("confirmed");

    private final String value;

    TicketStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
