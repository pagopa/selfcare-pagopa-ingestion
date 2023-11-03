package it.pagopa.selfcare.pagopa.ingestion.model.error;

import lombok.Builder;

@Builder
public class ProblemError {
    private String code;
    private String detail;
}