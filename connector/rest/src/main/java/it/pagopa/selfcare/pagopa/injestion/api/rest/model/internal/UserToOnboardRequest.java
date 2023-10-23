package it.pagopa.selfcare.pagopa.injestion.api.rest.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserToOnboardRequest {

    @JsonProperty("taxCode")
    private String taxCode;

    @JsonProperty("role")
    private PartyRole role;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("email")
    private String email;
}
