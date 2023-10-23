package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.dto.AutoApprovalOnboarding;

public interface InternalApiConnector {

    void autoApprovalOnboarding(String externalInstitutionId, String productId, AutoApprovalOnboarding request);

}
