package it.pagopa.selfcare.pagopa.injestion.api.rest.client;

import it.pagopa.selfcare.pagopa.injestion.api.rest.model.external.DelegationRequest;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal.AutoApprovalOnboardingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "${rest-client.external-api.serviceCode}", url = "${rest-client.external-api.base-url}")
public interface ExternalApiRestClient {

    @PostMapping(value = "${rest-client.external-api.createDelegation.path}", consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    void createDelegation(@RequestBody @Valid DelegationRequest request, @RequestHeader(name="Ocp-Apim-Subscription-Key")  String subscriptionKey);

    @PostMapping(value = "${rest-client.internal-api.autoApprovalOnboarding.path}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    void autoApprovalOnboarding(@PathVariable("externalInstitutionId") String externalInstitutionId,
                                @PathVariable("productId") String productId,
                                @RequestBody AutoApprovalOnboardingRequest request,
                                @RequestHeader(name="Ocp-Apim-Subscription-Key")  String subscriptionKey);

}
