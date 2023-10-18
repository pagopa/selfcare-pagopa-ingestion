package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.dto.LegalAddress;

public interface NationalRegistriesConnector {

    LegalAddress getLegalAddress(String taxCode);

}
