package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.dto.InstitutionProxyInfo;

public interface PartyRegistryProxyConnector {

    InstitutionProxyInfo getInstitutionById(String taxCode);

}
