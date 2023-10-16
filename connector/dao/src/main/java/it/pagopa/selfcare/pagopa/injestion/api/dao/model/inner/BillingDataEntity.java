package it.pagopa.selfcare.pagopa.injestion.api.dao.model.inner;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants(asEnum = true)
public class BillingDataEntity {

    private String businessName;
    private String digitalAddress;
    private String recipientCode;
    private String registeredOffice;
    private String taxCode;
    private String vatNumber;
    private String zipCode;
}
