package com.oris.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PricingAdjustmentType {
    PERCENT("per"),
    ABSOLUTE("abs");

    private final String value;

    PricingAdjustmentType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
