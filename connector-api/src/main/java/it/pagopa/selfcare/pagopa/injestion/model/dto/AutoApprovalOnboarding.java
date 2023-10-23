package it.pagopa.selfcare.pagopa.injestion.model.dto;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.commons.base.utils.Origin;
import lombok.Data;

import java.util.List;

@Data
public class AutoApprovalOnboarding {

    private List<UserToOnboard> users;
    private BillingData billingData;
    private InstitutionType institutionType;
    private Origin origin;

}