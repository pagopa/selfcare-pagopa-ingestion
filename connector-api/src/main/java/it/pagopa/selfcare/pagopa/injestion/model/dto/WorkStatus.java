package it.pagopa.selfcare.pagopa.injestion.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum WorkStatus {
    NOT_WORKED("NOT_WORKED"),
    TO_SEND("TO_SEND"),
    TO_BUILDING("TO_BUILDING"),
    ERROR("ERROR"),
    DONE("DONE");

    private final String value;

    WorkStatus(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @JsonCreator
    public static WorkStatus fromValue(String value) {
        return Arrays.stream(values())
                .filter(origin -> origin.toString().equals(value))
                .findAny()
                .orElseThrow();
    }

}