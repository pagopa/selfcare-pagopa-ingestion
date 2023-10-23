package it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.pagopa.injestion.model.dto.AssistanceContacts;
import lombok.Data;

import java.util.List;

@Data
public class AutoApprovalOnboardingRequest {

    @JsonProperty("users")
    private List<UserToOnboardRequest> users;

    @JsonProperty("billingData")
    private BillingDataRequest billingData;

    @JsonProperty("institutionType")
    private InstitutionType institutionType;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("pricingPlan")
    private String pricingPlan;

    @JsonProperty("pspData")
    private PspDataRequest pspData;

    @JsonProperty("geographicTaxonomies")
    private List<GeographicTaxonomyRequest> geographicTaxonomies;

    @JsonProperty("companyInformations")
    private CompanyInformationsRequest companyInformations;

    @JsonProperty("assistanceContacts")
    private AssistanceContacts assistanceContacts;

}