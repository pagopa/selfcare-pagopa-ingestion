package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.LegalAddress;
import org.springframework.web.bind.annotation.RequestParam;

public interface PartyRegistryProxyConnector {

    LegalAddress getLegalAddress(@RequestParam("id") String id);

}
