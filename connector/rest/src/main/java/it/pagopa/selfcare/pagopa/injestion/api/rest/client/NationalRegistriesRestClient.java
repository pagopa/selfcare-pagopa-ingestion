package it.pagopa.selfcare.pagopa.injestion.api.rest.client;

import it.pagopa.selfcare.pagopa.injestion.api.rest.model.national_registries.GetAddressRegistroImpreseOKDto;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.national_registries.LegalAddressRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.national-registries.serviceCode}", url = "${rest-client.national-registries.base-url}")
public interface NationalRegistriesRestClient {

    @PostMapping(value = "${rest-client.national-registries.getLegalAddress.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    GetAddressRegistroImpreseOKDto getLegalAddress(@RequestBody LegalAddressRequest legalAddressRequest);


}
