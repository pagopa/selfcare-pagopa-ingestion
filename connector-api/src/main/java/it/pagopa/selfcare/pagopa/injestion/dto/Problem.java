package it.pagopa.selfcare.pagopa.injestion.dto;

import lombok.Data;

import java.util.List;

@Data
public class Problem {
    private Integer status;
    private List<ProblemError> errors;
}