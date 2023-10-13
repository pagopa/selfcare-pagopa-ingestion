package it.pagopa.selfcare.pagopa.injestion.dto;

import lombok.Builder;

@Builder
public class ProblemError {
    private String code;
    private String detail;
}