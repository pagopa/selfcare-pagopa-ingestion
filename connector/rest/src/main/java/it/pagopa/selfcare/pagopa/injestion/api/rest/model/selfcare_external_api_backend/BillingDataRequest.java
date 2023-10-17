package it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_external_api_backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BillingDataRequest {

    @ApiModelProperty(required = true)
    @JsonProperty("businessName")
    @NotBlank
    private String businessName;

    @ApiModelProperty(required = true)
    @JsonProperty("registeredOffice")
    @NotBlank
    private String registeredOffice;

    @ApiModelProperty(required = true)
    @JsonProperty("digitalAddress")
    @NotBlank
    private String digitalAddress;

    @ApiModelProperty(required = true)
    @JsonProperty("zipCode")
    @NotBlank
    private String zipCode;

    @ApiModelProperty(required = true)
    @JsonProperty("taxCode")
    @NotBlank
    private String taxCode;

    @ApiModelProperty(required = true)
    @JsonProperty("vatNumber")
    @NotBlank
    private String vatNumber;

    @ApiModelProperty(required = true)
    @JsonProperty("recipientCode")
    private String recipientCode;

    @JsonProperty("publicServices")
    private Boolean publicServices;
}
