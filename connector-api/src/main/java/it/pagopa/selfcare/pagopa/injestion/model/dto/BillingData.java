package it.pagopa.selfcare.pagopa.injestion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BillingData {

    private String businessName;
    private String digitalAddress;
    private String recipientCode;
    private String registeredOffice;
    private String taxCode;
    private String vatNumber;
    private String zipCode;
}
