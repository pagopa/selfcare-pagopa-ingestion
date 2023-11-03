package it.pagopa.selfcare.pagopa.ingestion.api.rest.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BillingDataRequest {

    @JsonProperty("businessName")
    private String businessName;

    @JsonProperty("registeredOffice")
    private String registeredOffice;

    @JsonProperty("digitalAddress")
    private String digitalAddress;

    @JsonProperty("zipCode")
    private String zipCode;

    @JsonProperty("taxCode")
    private String taxCode;

    @JsonProperty("vatNumber")
    private String vatNumber;

    @JsonProperty("recipientCode")
    private String recipientCode;

    @JsonProperty("publicServices")
    private Boolean publicServices;

}
