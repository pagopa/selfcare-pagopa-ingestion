package it.pagopa.selfcare.pagopa.ingestion.model.error;

import lombok.Data;

import java.util.List;

@Data
public class Problem {
    private Integer status;
    private List<ProblemError> errors;
}