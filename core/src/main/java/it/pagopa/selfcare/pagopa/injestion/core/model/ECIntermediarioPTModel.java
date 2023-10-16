package it.pagopa.selfcare.pagopa.injestion.core.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ECIntermediarioPTModel {

    @CsvBindByPosition(position = 0)
    private String enteIndirettoRagioneSociale;

    @CsvBindByPosition(position = 1)
    private String enteIndirettoCF;

    @CsvBindByPosition(position = 2)
    private String intermediarioPTRagioneSociale;

    @CsvBindByPosition(position = 3)
    private String intermediarioPTCF;

}