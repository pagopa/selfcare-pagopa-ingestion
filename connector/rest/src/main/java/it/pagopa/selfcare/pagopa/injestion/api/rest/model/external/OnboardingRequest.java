package it.pagopa.selfcare.pagopa.injestion.api.rest.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.pagopa.injestion.model.dto.UserToOnboard;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OnboardingRequest {

    @ApiModelProperty(required = true)
    @NotEmpty
    @Valid
    @JsonProperty("users")
    private List<UserToOnboard> users;

    @ApiModelProperty(required = true)
    @NotNull
    @Valid
    @JsonProperty("billingData")
    private BillingDataRequest billingData;

    @ApiModelProperty(required = true)
    @NotNull
    @JsonProperty("institutionType")
    private InstitutionType institutionType;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("pricingPlan")
    private String pricingPlan;

    @Valid
    @JsonProperty("pspData")
    private PspDataRequest pspData;

    @NotNull
    @Valid
    @JsonProperty("geographicTaxonomies")
    private List<GeographicTaxonomyRequest> geographicTaxonomies;

    @Valid
    @JsonProperty("companyInformations")
    private CompanyInformationsRequest companyInformations;

    @Valid
    @JsonProperty("assistanceContacts")
    private AssistanceContactsRequest assistanceContacts;

}
