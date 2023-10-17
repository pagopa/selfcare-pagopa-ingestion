package it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_external_api_backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompanyInformationsRequest {

    @JsonProperty("rea")
    private String rea;

    @JsonProperty("shareCapital")
    private String shareCapital;

    @JsonProperty("businessRegisterPlace")
    private String businessRegisterPlace;
}
