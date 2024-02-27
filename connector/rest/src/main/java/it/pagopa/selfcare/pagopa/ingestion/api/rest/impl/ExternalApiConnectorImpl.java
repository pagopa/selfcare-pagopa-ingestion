package it.pagopa.selfcare.pagopa.ingestion.api.rest.impl;

import feign.FeignException;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.ExternalApiConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.client.ExternalApiRestClient;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.client.InternalApiRestClient;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.model.external.CreatedAtRequest;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.model.internal.AutoApprovalOnboardingRequest;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.model.internal.DelegationRequest;
import it.pagopa.selfcare.pagopa.ingestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.AutoApprovalOnboarding;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.Delegation;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.InstitutionsResponse;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.OnboardingUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;


@Slf4j
@Service
public class ExternalApiConnectorImpl implements ExternalApiConnector {

    private final ExternalApiRestClient externalApiRestClient;

    public ExternalApiConnectorImpl(ExternalApiRestClient externalApiRestClient) {
        this.externalApiRestClient = externalApiRestClient;
    }

    @Override
    public InstitutionsResponse getInstitutions(String taxCode) {
        log.info("START - getInstitutions");
        try {
            InstitutionsResponse response = externalApiRestClient.getInstitutions(taxCode);
            Assert.notNull(response, "Response is null");
            Assert.notNull(response.getInstitutions(), "Institutions are null");
            if(response.getInstitutions().isEmpty())
                throw new ResourceNotFoundException("Institution not found");
            return response;
        } catch (FeignException e) {
            if(e.status() == 404)
                throw new ResourceNotFoundException("Institution not found");
            log.error("Error in getInstitutions", e);

            throw e;
        }
    }
}
