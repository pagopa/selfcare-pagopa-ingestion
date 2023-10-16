package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.dto.Institution;
import it.pagopa.selfcare.pagopa.injestion.model.dto.LegalAddress;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface PartyRegistryProxyConnector {

    LegalAddress getLegalAddress(@RequestParam("id") String id);

    Institution findInstitution(@PathVariable(value = "id") String taxId, @RequestParam(value = "origin", required = false) String origin, @RequestParam(value = "categories", required = false) Optional<String> categories);


}
