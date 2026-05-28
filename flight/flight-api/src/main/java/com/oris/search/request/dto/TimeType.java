package com.oris.search.request.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TimeType {
    MORNING(6,12, "morning"),
    AFTERNOON(12, 18, "afternoon"),
    EVENING(18, 24, "evening"),
    NIGHT(0, 6, "night");

    private final int start;
    private final int end;
    private final String value;

    TimeType(int start, int end, String value) {
        this.start = start;
        this.end = end;
        this.value = value;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
