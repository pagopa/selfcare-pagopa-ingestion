package it.pagopa.selfcare.pagopa.ingestion.model.csv;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ECPTRelationshipModel {

    @CsvBindByPosition(position = 0)
    private String enteIndirettoRagioneSociale;

    @CsvBindByPosition(position = 1)
    private String enteIndirettoCF;

    @CsvBindByPosition(position = 2)
    private String intermediarioPTRagioneSociale;

    @CsvBindByPosition(position = 3)
    private String intermediarioPTCF;

}
