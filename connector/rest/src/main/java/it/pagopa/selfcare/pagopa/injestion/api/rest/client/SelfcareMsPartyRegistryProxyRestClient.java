package it.pagopa.selfcare.pagopa.injestion.api.rest.client;

import it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_ms_party_registry_proxy.IniPecResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.selfcare-ms-party-registry-proxy.serviceCode}", url = "${rest-client.selfcare-ms-party-registry-proxy.serviceUrl}")
public interface SelfcareMsPartyRegistryProxyRestClient {

    @PostMapping(value = "${rest-client.selfcare-ms-party-registry-proxy.infocamere.inipec.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    IniPecResponse getInipec(String taxCode);


}
