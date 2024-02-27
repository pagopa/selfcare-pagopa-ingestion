package it.pagopa.selfcare.pagopa.ingestion.api.rest;

import it.pagopa.selfcare.pagopa.ingestion.model.dto.InstitutionsResponse;

public interface ExternalApiConnector {
    InstitutionsResponse getInstitutions(String taxCode);
}
