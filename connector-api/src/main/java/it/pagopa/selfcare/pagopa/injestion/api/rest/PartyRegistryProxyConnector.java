package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.dto.InstitutionProxyInfo;
import it.pagopa.selfcare.pagopa.injestion.model.dto.LegalAddress;

public interface PartyRegistryProxyConnector {

    InstitutionProxyInfo getInstitutionById(String taxCode);

    LegalAddress getLegalAddress(String taxId);

}
