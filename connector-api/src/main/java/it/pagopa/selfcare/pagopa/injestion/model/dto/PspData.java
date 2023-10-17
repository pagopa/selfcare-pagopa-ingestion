package it.pagopa.selfcare.pagopa.injestion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PspData {

    private String businessRegisterNumber;

    private String legalRegisterName;

    private String legalRegisterNumber;

    private String abiCode;

    private Boolean vatNumberGroup;

    private DpoData dpoData;
}
