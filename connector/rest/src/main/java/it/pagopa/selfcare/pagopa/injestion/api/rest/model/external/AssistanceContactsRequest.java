package it.pagopa.selfcare.pagopa.injestion.api.rest.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class AssistanceContactsRequest {

    @Email
    @JsonProperty("supportEmail")
    private String supportEmail;

    @JsonProperty("supportPhone")
    private String supportPhone;
}
