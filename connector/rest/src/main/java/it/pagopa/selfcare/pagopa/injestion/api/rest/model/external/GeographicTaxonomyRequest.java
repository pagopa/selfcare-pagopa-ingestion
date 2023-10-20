package it.pagopa.selfcare.pagopa.injestion.api.rest.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GeographicTaxonomyRequest {

    @ApiModelProperty(required = true)
    @JsonProperty("code")
    @NotBlank
    private String code;

    @ApiModelProperty(required = true)
    @JsonProperty("desc")
    @NotBlank
    private String desc;
}
