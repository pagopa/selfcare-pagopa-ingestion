package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.injestion.api.rest.SelfcareExternalApiBackendConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.SelfcareExternalApiBackendRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_external_api_backend.*;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SelfcareExternalApiBackendConnectorImpl implements SelfcareExternalApiBackendConnector {

    private final SelfcareExternalApiBackendRestClient selfcareExternalApiBackendRestClient;

    public SelfcareExternalApiBackendConnectorImpl(SelfcareExternalApiBackendRestClient selfcareExternalApiBackendRestClient) {
        this.selfcareExternalApiBackendRestClient = selfcareExternalApiBackendRestClient;
    }

    @Override
    public void autoApprovalOnboarding(String externalInstitutionId, String productId, Onboarding request) {
        log.info("autoApprovalOnboarding");
        selfcareExternalApiBackendRestClient.autoApprovalOnboarding(externalInstitutionId, productId, toOnboardingRequest(request));
    }

    private OnboardingRequest toOnboardingRequest(Onboarding onboarding) {
        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setOrigin(onboarding.getOrigin());
        onboardingRequest.setInstitutionType(onboarding.getInstitutionType());
        onboardingRequest.setCompanyInformations(toCompanyInformationsRequest(onboarding.getCompanyInformation()));
        onboardingRequest.setBillingData(toBillingDataRequest(onboarding.getBillingData()));
        onboardingRequest.setUsers(toUserRequest(onboarding.getUsers()));
        onboardingRequest.setAssistanceContacts(toAssistanceContactsRequest(onboarding.getAssistanceContacts()));
        onboardingRequest.setPricingPlan(onboarding.getPricingPlan());
        onboardingRequest.setPspData(toPspDataRequest(onboarding.getPspData()));
        onboardingRequest.setGeographicTaxonomies(toGeographicTaxonomiesRequest(onboarding.getGeographicTaxonomies()));
        return onboardingRequest;
    }

    private List<GeographicTaxonomyRequest> toGeographicTaxonomiesRequest(List<GeographicTaxonomies> geographicTaxonomies) {
        return geographicTaxonomies.stream().map(this::toGeographicTaxonomyRequest).collect(Collectors.toList());
    }

    private GeographicTaxonomyRequest toGeographicTaxonomyRequest(GeographicTaxonomies geographicTaxonomy) {
        if (geographicTaxonomy != null) {
            GeographicTaxonomyRequest request = new GeographicTaxonomyRequest();
            request.setCode(geographicTaxonomy.getCode());
            request.setDesc(geographicTaxonomy.getDesc());
            return request;
        }
        return null;
    }

    private PspDataRequest toPspDataRequest(PspData pspData) {
        if (pspData != null) {
            PspDataRequest pspDataRequest = new PspDataRequest();
            pspDataRequest.setAbiCode(pspData.getAbiCode());
            pspDataRequest.setBusinessRegisterNumber(pspData.getBusinessRegisterNumber());
            pspDataRequest.setVatNumberGroup(pspData.getVatNumberGroup());
            pspDataRequest.setBusinessRegisterNumber(pspData.getBusinessRegisterNumber());
            pspDataRequest.setLegalRegisterName(pspData.getLegalRegisterName());
            pspDataRequest.setLegalRegisterNumber(pspData.getLegalRegisterNumber());
            pspDataRequest.setDpoData(toDpoDataRequest(pspData.getDpoData()));
            return pspDataRequest;
        }
        return null;
    }

    private DpoDataRequest toDpoDataRequest(DpoData dpoData) {
        if (dpoData != null) {
            DpoDataRequest dpoDataRequest = new DpoDataRequest();
            dpoDataRequest.setAddress(dpoData.getAddress());
            dpoDataRequest.setPec(dpoData.getPec());
            dpoDataRequest.setEmail(dpoData.getEmail());
            return dpoDataRequest;
        }
        return null;
    }

    private AssistanceContactsRequest toAssistanceContactsRequest(AssistanceContacts assistanceContacts) {
        AssistanceContactsRequest assistanceContactsRequest = new AssistanceContactsRequest();
        assistanceContactsRequest.setSupportEmail(assistanceContacts.getSupportEmail());
        assistanceContactsRequest.setSupportPhone(assistanceContacts.getSupportPhone());
        return assistanceContactsRequest;
    }

    private List<UserRequest> toUserRequest(List<User> users) {
        return users.stream().map(this::toUserRequest).collect(Collectors.toList());
    }

    private UserRequest toUserRequest(User user) {
        if (user != null) {
            UserRequest userRequest = new UserRequest();
            userRequest.setEmail(user.getEmail());
            userRequest.setName(user.getName());
            userRequest.setRole(user.getPartyRole());
            userRequest.setProductRole(user.getProductRole());
            userRequest.setSurname(user.getSurname());
            userRequest.setTaxCode(user.getTaxCode());
            return userRequest;
        }
        return null;
    }

    private CompanyInformationsRequest toCompanyInformationsRequest(CompanyInformation companyInformation) {
        if (companyInformation != null) {
            CompanyInformationsRequest companyInformationsRequest = new CompanyInformationsRequest();
            companyInformationsRequest.setRea(companyInformation.getRea());
            companyInformationsRequest.setShareCapital(companyInformation.getShareCapital());
            companyInformationsRequest.setBusinessRegisterPlace(companyInformation.getBusinessRegisterPlace());
            return companyInformationsRequest;
        }
        return null;
    }

    private BillingDataRequest toBillingDataRequest(BillingData billingData) {
        if (billingData != null) {
            BillingDataRequest billingDataRequest = new BillingDataRequest();
            billingDataRequest.setBusinessName(billingData.getBusinessName());
            billingDataRequest.setTaxCode(billingData.getTaxCode());
            billingDataRequest.setZipCode(billingData.getZipCode());
            billingDataRequest.setPublicServices(billingData.getPublicServices());
            billingDataRequest.setRegisteredOffice(billingData.getRegisteredOffice());
            billingDataRequest.setRecipientCode(billingData.getRecipientCode());
            billingDataRequest.setVatNumber(billingData.getVatNumber());
            billingDataRequest.setDigitalAddress(billingData.getDigitalAddress());
            return billingDataRequest;
        }
        return null;
    }
}
