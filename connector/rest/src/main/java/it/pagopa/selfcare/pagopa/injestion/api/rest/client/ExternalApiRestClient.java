package it.pagopa.selfcare.pagopa.injestion.api.rest.client;

import it.pagopa.selfcare.pagopa.injestion.api.rest.model.external.DelegationRequest;
import it.pagopa.selfcare.pagopa.injestion.model.dto.AutoApprovalOnboardingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.selfcare-external-api.serviceCode}", url = "${rest-client.selfcare-external-api.base-url}")
public interface ExternalApiRestClient {

    @PostMapping(value = "${rest-client.external-api.onboarding.createDelegation.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    void createDelegation(@RequestBody @Valid DelegationRequest request);

}
