package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.dto.AutoApprovalOnboarding;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Delegation;

public interface ExternalApiConnector {

    void createDelegation(Delegation delegation);

    void autoApprovalOnboarding(String externalInstitutionId, String productId, AutoApprovalOnboarding request);
}
