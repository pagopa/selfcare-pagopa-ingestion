package it.pagopa.selfcare.pagopa.injestion.api.rest.model.national_registries;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LegalAddressFilter {

    @JsonProperty("taxId")
    private String taxId;

}
