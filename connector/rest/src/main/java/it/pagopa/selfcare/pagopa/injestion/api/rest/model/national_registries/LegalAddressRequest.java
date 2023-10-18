package it.pagopa.selfcare.pagopa.injestion.api.rest.model.national_registries;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LegalAddressRequest {

    @JsonProperty("filter")
    private LegalAddressFilter filter;
}