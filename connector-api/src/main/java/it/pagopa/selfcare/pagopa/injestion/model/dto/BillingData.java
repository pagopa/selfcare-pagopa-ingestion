package it.pagopa.selfcare.pagopa.injestion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BillingData {

    private String businessName;
    private String registeredOffice;
    private String digitalAddress;
    private String zipCode;
    private String taxCode;
    private String vatNumber;
    private String recipientCode;
    private Boolean publicServices;

}
