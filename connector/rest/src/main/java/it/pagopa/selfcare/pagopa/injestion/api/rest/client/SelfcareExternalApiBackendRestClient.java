package it.pagopa.selfcare.pagopa.injestion.api.rest.client;

import it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_external_api_backend.OnboardingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.selfcare-external-api-backend.serviceCode}", url = "${rest-client.selfcare-external-api-backend.serviceUrl}")
public interface SelfcareExternalApiBackendRestClient {

    @PostMapping(value = "${rest-client.selfcare-external-api-backend.onboarding.autoApprovalOnboarding.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    void autoApprovalOnboarding(@PathVariable("externalInstitutionId") String externalInstitutionId, @PathVariable("productId") String productId, @RequestBody @Valid OnboardingRequest request);

}
