package it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import lombok.Data;
import it.pagopa.selfcare.commons.base.utils.Origin;

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
    private Origin origin;

}