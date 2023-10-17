package it.pagopa.selfcare.pagopa.injestion.api.rest.client;

import it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_ms_core.InstitutionFromIpaPost;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_ms_core.InstitutionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.selfcare-ms-core.serviceCode}", url = "${rest-client.selfcare-ms-core.serviceUrl}")
public interface SelfcareMsCoreRestClient {

    @PostMapping(value = "${rest-client.selfcare-ms-core.createInstitutionFromIpa.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    InstitutionResponse createInstitutionFromIpa(@RequestBody @Valid InstitutionFromIpaPost institution);


}
