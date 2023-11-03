package it.pagopa.selfcare.pagopa.ingestion.api.rest.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeographicTaxonomyRequest {

    @JsonProperty("code")
    private String code;

    @JsonProperty("desc")
    private String desc;

}
