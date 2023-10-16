package it.pagopa.selfcare.pagopa.injestion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ECIntermediarioPT {

    private String enteIndirettoRagioneSociale;
    private String enteIndirettoCF;
    private String intermediarioPTRagioneSociale;
    private String intermediarioPTCF;

}
