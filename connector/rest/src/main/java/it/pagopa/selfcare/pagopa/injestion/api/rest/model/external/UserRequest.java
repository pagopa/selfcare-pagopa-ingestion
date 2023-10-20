package it.pagopa.selfcare.pagopa.injestion.api.rest.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserRequest {

    @ApiModelProperty(required = true)
    @JsonProperty("name")
    @NotBlank
    private String name;

    @ApiModelProperty(required = true)
    @JsonProperty("surname")
    @NotBlank
    private String surname;

    @ApiModelProperty(required = true)
    @JsonProperty("taxCode")
    @NotBlank
    private String taxCode;

    @ApiModelProperty(required = true)
    @JsonProperty("role")
    @NotNull
    private PartyRole role;

    @ApiModelProperty(required = true)
    @JsonProperty("email")
    @NotNull
    @Email
    private String email;

    @ApiModelProperty(hidden = true)
    @JsonProperty("productRole")
    private String productRole;

}
