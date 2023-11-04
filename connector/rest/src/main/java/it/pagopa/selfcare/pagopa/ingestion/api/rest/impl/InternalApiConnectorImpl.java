package it.pagopa.selfcare.pagopa.ingestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.ingestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.client.InternalApiRestClient;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.model.internal.DelegationRequest;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.model.internal.AutoApprovalOnboardingRequest;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.AutoApprovalOnboarding;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.Delegation;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.OnboardingUserRequest;
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
    public void createDelegation(Delegation delegation){
        internalApiRestClient.createDelegation(toDelegationRequest(delegation));
    }

    @Override
    public void autoApprovalOnboarding(String injectionInstitutionType, AutoApprovalOnboarding request) {
        log.info("autoApprovalOnboarding");
        internalApiRestClient.autoApprovalOnboarding(injectionInstitutionType, createRequestOnboarding(request));
    }

    @Override
    public void onboardingUser(OnboardingUserRequest request) {
        log.info("START - onboardingOperators");
        internalApiRestClient.onboardingUsers(request);
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
