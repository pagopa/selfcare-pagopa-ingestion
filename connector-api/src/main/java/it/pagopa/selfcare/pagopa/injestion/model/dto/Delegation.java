package it.pagopa.selfcare.pagopa.injestion.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

}