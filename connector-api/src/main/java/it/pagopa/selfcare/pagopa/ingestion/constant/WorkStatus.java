package it.pagopa.selfcare.pagopa.ingestion.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum WorkStatus {
    NOT_WORKED("NOT_WORKED"),
    ERROR("ERROR"),
    NOT_FOUND_IN_REGISTRY("NOT_FOUND_IN_REGISTRY"),
    MANAGER_NOT_FOUND("MANAGER_NOT_FOUND"),
    EMPTY_MANAGER_CF("EMPTY_MANAGER_CF"),
    EMPTY_USER_CF("EMPTY_USER_CF"),
    TO_SEND("TO_SEND"),
    ALREADY_ONBOARDED("ALREADY_ONBOARDED"),
    DONE("DONE"),
    INSTITUTION_NOT_FOUND("INSTITUTION_NOT_FOUND"),
    TOO_MANY_INSTITUTIONS("TOO_MANY_INSTITUTIONS");

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