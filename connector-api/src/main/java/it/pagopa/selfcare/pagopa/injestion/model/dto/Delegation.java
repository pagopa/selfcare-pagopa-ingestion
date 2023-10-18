package it.pagopa.selfcare.pagopa.injestion.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Delegation {

    private String id;
    private String from;
    private String institutionFromName;
    private String institutionToName;
    private String institutionFromRootName;
    private DelegationType type;
    private String to;
    private String productId;
    private InstitutionType institutionType;
    private String taxCode;
    private InstitutionType brokerType;
    private String brokerTaxCode;

}