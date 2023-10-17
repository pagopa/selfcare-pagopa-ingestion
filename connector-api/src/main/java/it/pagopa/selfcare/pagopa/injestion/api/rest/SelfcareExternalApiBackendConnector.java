package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.dto.Onboarding;

public interface SelfcareExternalApiBackendConnector {

    void autoApprovalOnboarding(String externalInstitutionId, String productId, Onboarding request);

}
