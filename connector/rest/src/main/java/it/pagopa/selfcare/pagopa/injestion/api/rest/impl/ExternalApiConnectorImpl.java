package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.injestion.api.rest.ExternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.ExternalApiRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.external.DelegationRequest;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal.AutoApprovalOnboardingRequest;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal.BillingDataRequest;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal.UserToOnboardRequest;
import it.pagopa.selfcare.pagopa.injestion.model.dto.AutoApprovalOnboarding;
import it.pagopa.selfcare.pagopa.injestion.model.dto.BillingData;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Delegation;
import it.pagopa.selfcare.pagopa.injestion.model.dto.UserToOnboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ExternalApiConnectorImpl implements ExternalApiConnector {

    private final ExternalApiRestClient externalApiRestClient;

    private final String externalSubscriptionkey;

    public ExternalApiConnectorImpl(ExternalApiRestClient externalApiRestClient,
                                    @Value("${authorization.external-api.subscriptionKey}") String externalSubscriptionkey) {
        this.externalApiRestClient = externalApiRestClient;
        this.externalSubscriptionkey = externalSubscriptionkey;
    }

    @Override
    public void createDelegation(Delegation delegation){
        externalApiRestClient.createDelegation(toDelegationRequest(delegation), externalSubscriptionkey);
    }

    @Override
    public void autoApprovalOnboarding(String externalInstitutionId, String productId, AutoApprovalOnboarding request) {
        log.info("autoApprovalOnboarding");
        externalApiRestClient.autoApprovalOnboarding(externalInstitutionId, productId, createRequestOnboarding(request), externalSubscriptionkey);
    }

    private AutoApprovalOnboardingRequest createRequestOnboarding(AutoApprovalOnboarding request) {
        AutoApprovalOnboardingRequest autoApprovalOnboardingRequest = new AutoApprovalOnboardingRequest();
        autoApprovalOnboardingRequest.setUsers(createUserToOnboardRequests(request.getUsers()));
        autoApprovalOnboardingRequest.setBillingData(createBillingDataRequest(request.getBillingData()));
        autoApprovalOnboardingRequest.setInstitutionType(request.getInstitutionType());
        autoApprovalOnboardingRequest.setOrigin(request.getOrigin());
        return autoApprovalOnboardingRequest;
    }

    private BillingDataRequest createBillingDataRequest(BillingData billingData) {
        BillingDataRequest billingDataRequest = new BillingDataRequest();
        billingDataRequest.setBusinessName(billingData.getBusinessName());
        billingDataRequest.setTaxCode(billingData.getTaxCode());
        billingDataRequest.setZipCode(billingData.getZipCode());
        billingDataRequest.setVatNumber(billingData.getVatNumber());
        billingDataRequest.setPublicServices(billingData.getPublicServices());
        billingDataRequest.setDigitalAddress(billingData.getDigitalAddress());
        billingDataRequest.setRecipientCode(billingData.getRecipientCode());
        billingDataRequest.setRegisteredOffice(billingData.getRegisteredOffice());
        return billingDataRequest;
    }

    private List<UserToOnboardRequest> createUserToOnboardRequests(List<UserToOnboard> users) {
        return users.stream()
                .map(userToOnboard -> new UserToOnboardRequest(userToOnboard.getTaxCode(), userToOnboard.getRole(), userToOnboard.getName(), userToOnboard.getSurname(), userToOnboard.getEmail()))
                .collect(java.util.stream.Collectors.toList());
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
