package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.injestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.ExternalApiRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.InternalApiRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.external.*;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InternalApiConnectorImpl implements InternalApiConnector {

    private final InternalApiRestClient internalApiRestClient;

    public InternalApiConnectorImpl(InternalApiRestClient internalApiRestClient) {
        this.internalApiRestClient = internalApiRestClient;
    }

    @Override
    public void autoApprovalOnboarding(String externalInstitutionId, String productId, AutoApprovalOnboardingRequest request) {
        log.info("autoApprovalOnboarding");
        internalApiRestClient.autoApprovalOnboarding(externalInstitutionId, productId, request);
    }
}
