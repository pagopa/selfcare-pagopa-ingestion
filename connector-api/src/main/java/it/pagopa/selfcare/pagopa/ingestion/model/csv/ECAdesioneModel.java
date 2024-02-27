package it.pagopa.selfcare.pagopa.ingestion.model.csv;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ECAdesioneModel {
    @CsvBindByPosition(position = 0)
    private String businessName;

    @CsvBindByPosition(position = 1)
    private String taxCode;

    @CsvBindByPosition(position = 2)
    private String dateOfJoining;
}
