package it.pagopa.selfcare.pagopa.injestion.api.rest.model.registryproxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProxyInstitutionResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("originId")
    private String originId;

    @JsonProperty("o")
    private String o;

    @JsonProperty("ou")
    private String ou;

    @JsonProperty("aoo")
    private String aoo;

    @JsonProperty("taxCode")
    private String taxCode;

    @JsonProperty("category")
    private String category;

    @JsonProperty("description")
    private String description;

    @JsonProperty("digitalAddress")
    private String digitalAddress;

    @JsonProperty("address")
    private String address;

    @JsonProperty("zipCode")
    private String zipCode;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("istatCode")
    private String istatCode;
}