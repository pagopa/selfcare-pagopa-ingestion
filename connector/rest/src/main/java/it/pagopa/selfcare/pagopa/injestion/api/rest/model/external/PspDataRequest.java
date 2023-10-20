package it.pagopa.selfcare.pagopa.injestion.api.rest.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PspDataRequest {

    @ApiModelProperty(required = true)
    @JsonProperty("businessRegisterNumber")
    @NotBlank
    private String businessRegisterNumber;

    @ApiModelProperty(required = true)
    @JsonProperty("legalRegisterName")
    @NotBlank
    private String legalRegisterName;

    @ApiModelProperty(required = true)
    @JsonProperty("legalRegisterNumber")
    @NotBlank
    private String legalRegisterNumber;

    @ApiModelProperty(required = true)
    @JsonProperty("abiCode")
    @NotBlank
    private String abiCode;

    @ApiModelProperty(required = true)
    @JsonProperty("vatNumberGroup")
    @NotNull
    private Boolean vatNumberGroup;

    @ApiModelProperty(required = true)
    @NotNull
    @Valid
    @JsonProperty("dpoData")
    private DpoDataRequest dpoData;

}
