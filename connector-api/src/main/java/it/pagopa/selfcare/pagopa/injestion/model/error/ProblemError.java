package it.pagopa.selfcare.pagopa.injestion.model.error;

import lombok.Builder;

@Builder
public class ProblemError {
    private String code;
    private String detail;
}