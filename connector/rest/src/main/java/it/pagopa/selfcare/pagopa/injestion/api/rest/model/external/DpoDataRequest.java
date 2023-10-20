package it.pagopa.selfcare.pagopa.injestion.api.rest.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class DpoDataRequest {

    @ApiModelProperty(required = true)
    @JsonProperty("address")
    @NotBlank
    private String address;

    @ApiModelProperty(required = true)
    @JsonProperty("address")
    @NotBlank
    @Email
    private String pec;

    @ApiModelProperty(required = true)
    @JsonProperty("email")
    @NotBlank
    @Email
    private String email;

}
