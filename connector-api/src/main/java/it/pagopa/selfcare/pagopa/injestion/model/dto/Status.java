package it.pagopa.selfcare.pagopa.injestion.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Status {
    TO_SENT("TO_SENT"),
    TO_BUILDING("TO_BUILDING"),
    DONE("DONE");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @JsonCreator
    public static Status fromValue(String value) {
        return Arrays.stream(values())
                .filter(origin -> origin.toString().equals(value))
                .findAny()
                .orElseThrow();
    }

}