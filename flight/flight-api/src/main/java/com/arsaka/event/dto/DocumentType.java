package com.arsaka.event.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentType {
    PASSPORT("passport"),
    ID_CARD("id card");

    private final String value;

    DocumentType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
