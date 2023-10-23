package it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DpoDataRequest {

    @JsonProperty("address")
    private String address;

    @JsonProperty("pec")
    private String pec;

    @JsonProperty("email")
    private String email;
}