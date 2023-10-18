package it.pagopa.selfcare.pagopa.injestion.api.rest.model.party_registry_proxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NationalRegistriesProfessionalAddress {
    @JsonProperty("description")
    private String description;

    @JsonProperty("municipality")
    private String municipality;

    @JsonProperty("province")
    private String province;

    @JsonProperty("address")
    private String address;

    @JsonProperty("zipCode")
    private String zipCode;
}