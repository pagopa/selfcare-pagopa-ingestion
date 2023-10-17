package it.pagopa.selfcare.pagopa.injestion.model.dto;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Onboarding {

    private List<User> users;
    private BillingData billingData;
    private InstitutionType institutionType;
    private String origin;
    private String pricingPlan;
    private PspData pspData;
    private List<GeographicTaxonomies> geographicTaxonomies;
    private CompanyInformation companyInformation;
    private AssistanceContacts assistanceContacts;

}
