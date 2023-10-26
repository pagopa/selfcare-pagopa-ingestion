package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.injestion.api.rest.ExternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.ExternalApiRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.external.DelegationRequest;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal.AutoApprovalOnboardingRequest;
import it.pagopa.selfcare.pagopa.injestion.model.dto.AutoApprovalOnboarding;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Delegation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ExternalApiConnectorImpl implements ExternalApiConnector {

    private final ExternalApiRestClient externalApiRestClient;

    public ExternalApiConnectorImpl(ExternalApiRestClient externalApiRestClient) {
        this.externalApiRestClient = externalApiRestClient;
    }

    @Override
    public void createDelegation(Delegation delegation){
        externalApiRestClient.createDelegation(toDelegationRequest(delegation));
    }

    @Override
    public void autoApprovalOnboarding(String injectionInstitutionType, AutoApprovalOnboarding request) {
        log.info("autoApprovalOnboarding");
        externalApiRestClient.autoApprovalOnboarding(injectionInstitutionType, createRequestOnboarding(request));
    }

    private AutoApprovalOnboardingRequest createRequestOnboarding(AutoApprovalOnboarding request) {
        AutoApprovalOnboardingRequest autoApprovalOnboardingRequest = new AutoApprovalOnboardingRequest();
        autoApprovalOnboardingRequest.setUsers(request.getUsers());
        autoApprovalOnboardingRequest.setBusinessName(request.getBusinessName());
        autoApprovalOnboardingRequest.setTaxCode(request.getTaxCode());
        autoApprovalOnboardingRequest.setProductId(request.getProductId());
        autoApprovalOnboardingRequest.setVatNumber(request.getVatNumber());
        autoApprovalOnboardingRequest.setRecipientCode(request.getRecipientCode());
        return autoApprovalOnboardingRequest;
    }

    private DelegationRequest toDelegationRequest(Delegation delegation){
        DelegationRequest delegationRequest = new DelegationRequest();
        delegationRequest.setTo(delegation.getTo());
        delegationRequest.setFrom(delegation.getFrom());
        delegationRequest.setInstitutionToName(delegation.getInstitutionToName());
        delegationRequest.setInstitutionFromName(delegation.getInstitutionFromName());
        delegationRequest.setType(delegation.getType());
        delegationRequest.setProductId(delegation.getProductId());
        return delegationRequest;
    }
}
