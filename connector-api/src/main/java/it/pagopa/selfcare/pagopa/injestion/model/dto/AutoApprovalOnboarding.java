package it.pagopa.selfcare.pagopa.injestion.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AutoApprovalOnboarding {

    private List<UserToOnboard> users;
    private String productId;
    private String businessName;
    private String taxCode;
    private String vatNumber;
    private String recipientCode;
}