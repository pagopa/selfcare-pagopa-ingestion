package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.dto.IniPec;

public interface SelfcareMsPartyRegistryProxyConnector {

    IniPec getIniPec(String taxCode);

}
