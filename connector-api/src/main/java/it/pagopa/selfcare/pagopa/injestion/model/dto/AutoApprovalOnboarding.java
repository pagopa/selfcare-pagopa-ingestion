package it.pagopa.selfcare.pagopa.injestion.model.dto;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import lombok.Data;

import java.util.List;

@Data
public class AutoApprovalOnboarding {

    private List<UserToOnboard> users;
    private BillingData billingData;
    private InstitutionType institutionType;
    private String origin;
    private String pricingPlan;
    private PspData pspData;
    private List<GeographicTaxonomy> geographicTaxonomies;
    private CompanyInformations companyInformations;
    private AssistanceContacts assistanceContacts;

}