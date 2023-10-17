package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.dto.Onboarding;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface SelfcareExternalApiBackendConnector {

    void autoApprovalOnboarding(@PathVariable("externalInstitutionId") String externalInstitutionId, @PathVariable("productId") String productId, @RequestBody @Valid Onboarding request);

}
