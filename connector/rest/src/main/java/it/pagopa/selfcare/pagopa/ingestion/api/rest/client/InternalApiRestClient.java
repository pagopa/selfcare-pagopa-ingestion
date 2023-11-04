package it.pagopa.selfcare.pagopa.ingestion.api.rest.client;

import it.pagopa.selfcare.pagopa.ingestion.api.rest.model.internal.DelegationRequest;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.model.internal.AutoApprovalOnboardingRequest;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.OnboardingUserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.internal-api.serviceCode}", url = "${rest-client.internal-api.base-url}")
public interface InternalApiRestClient {

    @PostMapping(value = "${rest-client.internal-api.createDelegation.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    void createDelegation(@RequestBody @Valid DelegationRequest request);

    @PostMapping(value = "${rest-client.internal-api.autoApprovalOnboarding.path}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    void autoApprovalOnboarding(@PathVariable("injectionInstitutionType") String injectionInstitutionType,
                                @RequestBody AutoApprovalOnboardingRequest request);

    @PostMapping(value = "${rest-client.internal-api.onboardingOperator.path}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    void onboardingUsers(@RequestBody OnboardingUserRequest request);


}
