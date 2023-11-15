package it.pagopa.selfcare.pagopa.ingestion.api.rest.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.DelegationType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DelegationRequest {

    @NotBlank
    @JsonProperty("fromTaxCode")
    private String from;

    @NotBlank
    @JsonProperty("toTaxCode")
    private String to;

    @NotBlank
    @JsonProperty("institutionFromName")
    private String institutionFromName;

    @NotBlank
    @JsonProperty("institutionToName")
    private String institutionToName;

    @NotBlank
    @JsonProperty("productId")
    private String productId;

    @NotNull
    @JsonProperty("type")
    private DelegationType type;

}
