package com.assemble.backend.models.entities.holiday.api;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TemporalScope {
    FULL_DAY("FullDay"),
    HALF_DAY("HalfDay");

    private final String value;

    TemporalScope(String value) {
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

}
