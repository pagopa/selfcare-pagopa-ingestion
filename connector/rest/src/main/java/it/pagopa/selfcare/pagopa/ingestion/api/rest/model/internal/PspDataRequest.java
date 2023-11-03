package it.pagopa.selfcare.pagopa.ingestion.api.rest.model.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PspDataRequest {

    @JsonProperty("businessRegisterNumber")
    private String businessRegisterNumber;

    @JsonProperty("legalRegisterName")
    private String legalRegisterName;

    @JsonProperty("legalRegisterNumber")
    private String legalRegisterNumber;

    @JsonProperty("abiCode")
    private String abiCode;

    @JsonProperty("vatNumberGroup")
    private Boolean vatNumberGroup;

    @JsonProperty("dpoData")
    private DpoDataRequest dpoData;
}