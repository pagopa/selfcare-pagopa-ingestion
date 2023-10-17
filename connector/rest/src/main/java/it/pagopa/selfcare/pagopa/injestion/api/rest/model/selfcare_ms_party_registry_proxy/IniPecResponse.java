package it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_ms_party_registry_proxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IniPecResponse {

    @JsonProperty("zipCode")
    private String zipCode;

    @JsonProperty("digitalAddress")
    private String digitalAddress;
}
