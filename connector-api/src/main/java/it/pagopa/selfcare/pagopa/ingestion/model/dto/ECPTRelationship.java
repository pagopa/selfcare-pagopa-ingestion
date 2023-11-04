package it.pagopa.selfcare.pagopa.ingestion.model.dto;

import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ECPTRelationship {

    private String correlationId;
    private String enteIndirettoRagioneSociale;
    private String enteIndirettoCF;
    private String intermediarioPTRagioneSociale;
    private String intermediarioPTCF;
    private WorkStatus workStatus;
    private int createHttpStatus;
    private String createMessage;

}
