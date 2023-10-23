package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.injestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.InternalApiRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal.*;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InternalApiConnectorImpl implements InternalApiConnector {

    private final InternalApiRestClient internalApiRestClient;
    private final String internalSubscriptionkey;

    public InternalApiConnectorImpl(InternalApiRestClient internalApiRestClient,
                                    @Value("${authorization.internal-api.subscriptionKey}") String internalSubscriptionkey) {
        this.internalApiRestClient = internalApiRestClient;
        this.internalSubscriptionkey = internalSubscriptionkey;
    }

    @Override
    public void autoApprovalOnboarding(String externalInstitutionId, String productId, AutoApprovalOnboarding request) {
        log.info("autoApprovalOnboarding");
        internalApiRestClient.autoApprovalOnboarding(externalInstitutionId, productId, createRequestOnboarding(request), internalSubscriptionkey);
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
}
