package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.dto.AutoApprovalOnboarding;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Delegation;
import it.pagopa.selfcare.pagopa.injestion.model.dto.OnboardingUserRequest;

public interface ExternalApiConnector {

    void createDelegation(Delegation delegation);

    void autoApprovalOnboarding(String injectionInstitutionType, AutoApprovalOnboarding request);

    void onboardingUser(OnboardingUserRequest request);
}
