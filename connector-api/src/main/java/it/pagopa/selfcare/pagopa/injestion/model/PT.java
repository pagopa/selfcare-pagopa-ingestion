package it.pagopa.selfcare.pagopa.injestion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PT {

    private String businessName;
    private String taxCode;
    private String registeredOffice;
    private String zipCode;
    private String digitalAddress;
    private String vatNumber;

}