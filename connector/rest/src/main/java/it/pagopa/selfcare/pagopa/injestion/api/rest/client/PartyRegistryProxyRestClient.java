package it.pagopa.selfcare.pagopa.injestion.api.rest.client;

import it.pagopa.selfcare.pagopa.injestion.api.rest.model.party_registry_proxy.NationalRegistriesProfessionalAddress;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.party_registry_proxy.ProxyInstitutionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.party-registry-proxy.serviceCode}", url = "${rest-client.party-registry-proxy.base-url}")
public interface PartyRegistryProxyRestClient {

    @GetMapping(value = "${rest-client.party-registry-proxy.getInstitutionById.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    ProxyInstitutionResponse getInstitutionById(@PathVariable("institutionId") String id);


    @GetMapping(value = "${rest-client.party-registry-proxy.getLegalAddress.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    NationalRegistriesProfessionalAddress getLegalAddress(@RequestParam(value = "taxId") String taxId);

}
