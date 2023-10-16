package it.pagopa.selfcare.pagopa.injestion.api.rest.client;

import it.pagopa.selfcare.pagopa.injestion.api.rest.model.InstitutionResponse;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.LegalAddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.party-registry-proxy.serviceCode}", url = "${rest-client.party-registry-proxy.serviceUrl}")
public interface PartyRegistryProxyRestClient {

    @PostMapping(value = "${rest-client.party-registry-proxy.infocamere.legalAddress.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    LegalAddressResponse getLegalAddress(@RequestParam(value = "taxId") String taxId);

    @PostMapping(value = "${rest-client.party-registry-proxy.ipa.findInstitution.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    InstitutionResponse findInstitution(@PathVariable(value = "id") String taxId, @RequestParam(value = "origin", required = false) String origin, @RequestParam(value = "categories", required = false) Optional<String> categories);


}
