package it.pagopa.selfcare.pagopa.injestion.api.rest.model.party_registry_proxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LegalAddressResponse {

    @JsonProperty("address")
    private String address;

    @JsonProperty("zipCode")
    private String zipCode;

}
