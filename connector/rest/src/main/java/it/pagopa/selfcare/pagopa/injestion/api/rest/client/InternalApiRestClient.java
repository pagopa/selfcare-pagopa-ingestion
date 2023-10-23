package it.pagopa.selfcare.pagopa.injestion.api.rest.client;

import it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal.AutoApprovalOnboardingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${rest-client.internal-api.serviceCode}", url = "${rest-client.internal-api.base-url}")
public interface InternalApiRestClient {

    @PostMapping(value = "${rest-client.internal-api.autoApprovalOnboarding.path}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    void autoApprovalOnboarding(@PathVariable("externalInstitutionId") String externalInstitutionId,
                                @PathVariable("productId") String productId,
                                @RequestBody AutoApprovalOnboardingRequest request,
                                @RequestHeader(name="Ocp-Apim-Subscription-Key")  String subscriptionKey);
}
