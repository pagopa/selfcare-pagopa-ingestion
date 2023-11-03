package it.pagopa.selfcare.pagopa.ingestion.api.rest.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AssistanceContactsRequest {

    @JsonProperty("supportEmail")
    private String supportEmail;

    @JsonProperty("supportPhone")
    private String supportPhone;
}
