package it.pagopa.selfcare.pagopa.ingestion.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Delegation {

    private String from;
    private String to;
    private String institutionFromName;
    private String institutionToName;
    private String productId;
    private DelegationType type;
    private String onboardingMessage;

}