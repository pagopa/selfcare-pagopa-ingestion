package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.dto.AutoApprovalOnboardingRequest;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Delegation;

public interface InternalApiConnector {

    void autoApprovalOnboarding(String externalInstitutionId, String productId, AutoApprovalOnboardingRequest request);

}
