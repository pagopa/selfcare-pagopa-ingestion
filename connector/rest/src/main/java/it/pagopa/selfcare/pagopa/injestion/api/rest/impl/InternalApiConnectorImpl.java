package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.injestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.InternalApiRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal.*;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InternalApiConnectorImpl implements InternalApiConnector {

    private final InternalApiRestClient internalApiRestClient;

    public InternalApiConnectorImpl(InternalApiRestClient internalApiRestClient) {
        this.internalApiRestClient = internalApiRestClient;
    }

    @Override
    public void autoApprovalOnboarding(String externalInstitutionId, String productId, AutoApprovalOnboarding request) {
        log.info("autoApprovalOnboarding");
        internalApiRestClient.autoApprovalOnboarding(externalInstitutionId, productId, createRequestOnboarding(request));
    }

    private AutoApprovalOnboardingRequest createRequestOnboarding(AutoApprovalOnboarding request) {
        AutoApprovalOnboardingRequest autoApprovalOnboardingRequest = new AutoApprovalOnboardingRequest();
        autoApprovalOnboardingRequest.setUsers(createUserToOnboardRequests(request.getUsers()));
        autoApprovalOnboardingRequest.setBillingData(createBillingDataRequest(request.getBillingData()));
        autoApprovalOnboardingRequest.setInstitutionType(request.getInstitutionType());
        autoApprovalOnboardingRequest.setOrigin(request.getOrigin());
        autoApprovalOnboardingRequest.setPricingPlan(request.getPricingPlan());
        autoApprovalOnboardingRequest.setPspData(createPspDataRequest(request.getPspData()));
        autoApprovalOnboardingRequest.setGeographicTaxonomies(createGeographicTaxonomiesRequest(request.getGeographicTaxonomies()));
        autoApprovalOnboardingRequest.setCompanyInformations(createCompanyInformationsRequest(request.getCompanyInformations()));
        autoApprovalOnboardingRequest.setAssistanceContacts(request.getAssistanceContacts());
        return autoApprovalOnboardingRequest;
    }

    private CompanyInformationsRequest createCompanyInformationsRequest(CompanyInformations companyInformations) {
        CompanyInformationsRequest companyInformationsRequest = new CompanyInformationsRequest();
        companyInformationsRequest.setRea(companyInformations.getRea());
        companyInformationsRequest.setShareCapital(companyInformations.getShareCapital());
        companyInformationsRequest.setBusinessRegisterPlace(companyInformations.getBusinessRegisterPlace());
        return companyInformationsRequest;
    }

    private List<GeographicTaxonomyRequest> createGeographicTaxonomiesRequest(List<GeographicTaxonomy> geographicTaxonomies){
        return geographicTaxonomies.stream().map(this::createGeographicTaxonomyRequest).collect(Collectors.toList());
    }

    private GeographicTaxonomyRequest createGeographicTaxonomyRequest(GeographicTaxonomy geographicTaxonomy) {
        GeographicTaxonomyRequest geographicTaxonomyRequest = new GeographicTaxonomyRequest();
        geographicTaxonomyRequest.setCode(geographicTaxonomy.getCode());
        geographicTaxonomyRequest.setDesc(geographicTaxonomy.getDesc());
        return geographicTaxonomyRequest;
    }

    private PspDataRequest createPspDataRequest(PspData pspData) {
        PspDataRequest pspDataRequest = new PspDataRequest();
        pspDataRequest.setAbiCode(pspData.getAbiCode());
        pspDataRequest.setBusinessRegisterNumber(pspData.getBusinessRegisterNumber());
        pspDataRequest.setLegalRegisterName(pspData.getLegalRegisterName());
        pspDataRequest.setLegalRegisterNumber(pspData.getLegalRegisterNumber());
        pspDataRequest.setVatNumberGroup(pspData.getVatNumberGroup());
        pspDataRequest.setDpoData(crateDpoDataRequest(pspData.getDpoData()));
        return pspDataRequest;
    }

    private DpoDataRequest crateDpoDataRequest(DpoData dpoData) {
        DpoDataRequest dpoDataRequest = new DpoDataRequest();
        dpoDataRequest.setAddress(dpoData.getAddress());
        dpoDataRequest.setPec(dpoData.getPec());
        dpoDataRequest.setEmail(dpoData.getEmail());
        return dpoDataRequest;
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
