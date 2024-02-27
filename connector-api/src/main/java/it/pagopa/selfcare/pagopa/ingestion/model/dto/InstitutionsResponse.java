package it.pagopa.selfcare.pagopa.ingestion.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class InstitutionsResponse {
    private List<InstitutionResponse> institutions;
}
