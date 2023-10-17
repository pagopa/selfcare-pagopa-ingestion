package it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_ms_core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InstitutionFromIpaPost {

    @NotNull
    @JsonProperty("taxCode")
    private String taxCode;

    @JsonProperty("subunitCode")
    private String subunitCode;

    @JsonProperty("subunitType")
    private InstitutionPaSubunitType subunitType;
}