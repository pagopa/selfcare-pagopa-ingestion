package it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal;

import it.pagopa.selfcare.pagopa.injestion.model.dto.UserToOnboard;
import lombok.Data;
import java.util.List;

@Data
public class AutoApprovalOnboardingRequest {

    private List<UserToOnboard> users;
    private String businessName;
    private String taxCode;
    private String productId;
    private String vatNumber;
    private String recipientCode;

}