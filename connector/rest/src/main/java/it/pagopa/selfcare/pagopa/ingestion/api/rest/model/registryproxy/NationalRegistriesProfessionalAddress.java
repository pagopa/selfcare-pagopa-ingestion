package it.pagopa.selfcare.pagopa.ingestion.api.rest.model.registryproxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NationalRegistriesProfessionalAddress {

    @JsonProperty("address")
    private String address;

    @JsonProperty("zipCode")
    private String zipCode;
}