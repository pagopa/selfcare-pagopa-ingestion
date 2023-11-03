package it.pagopa.selfcare.pagopa.ingestion.api.rest;

import it.pagopa.selfcare.pagopa.ingestion.model.dto.AutoApprovalOnboarding;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.Delegation;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.OnboardingUserRequest;

public interface ExternalApiConnector {

    void createDelegation(Delegation delegation);

    void autoApprovalOnboarding(String injectionInstitutionType, AutoApprovalOnboarding request);

    void onboardingUser(OnboardingUserRequest request);
}
