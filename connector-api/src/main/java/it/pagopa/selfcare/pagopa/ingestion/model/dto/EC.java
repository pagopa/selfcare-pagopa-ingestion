package it.pagopa.selfcare.pagopa.ingestion.model.dto;

import it.pagopa.selfcare.commons.base.utils.Origin;
import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EC {

    private String id;
    private String businessName;
    private String taxCode;
    private String registeredOffice;
    private String zipCode;
    private String digitalAddress;
    private String vatNumber;
    private String recipientCode;
    private Origin origin;
    private WorkStatus workStatus;
    private int onboardingHttpStatus;

}