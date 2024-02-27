package it.pagopa.selfcare.pagopa.ingestion.api.rest;

import it.pagopa.selfcare.pagopa.ingestion.model.dto.AutoApprovalOnboarding;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.Delegation;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.InstitutionsResponse;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.OnboardingUserRequest;

import java.time.LocalDate;

public interface InternalApiConnector {

    void createDelegation(Delegation delegation);

    void autoApprovalOnboarding(String injectionInstitutionType, AutoApprovalOnboarding request);

    void onboardingUser(OnboardingUserRequest request);

    void updateCreatedAt(String institutionId, String dateOfJoining);
}
