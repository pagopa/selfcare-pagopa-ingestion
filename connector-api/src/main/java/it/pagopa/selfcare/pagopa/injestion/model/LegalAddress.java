package it.pagopa.selfcare.pagopa.injestion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LegalAddress {

    @JsonProperty("address")
    private String address;

    @JsonProperty("zipCode")
    private String zipCode;

}
