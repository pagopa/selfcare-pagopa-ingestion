package it.pagopa.selfcare.pagopa.ingestion.model.csv;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    @CsvBindByPosition(position = 0)
    private String name;

    @CsvBindByPosition(position = 1)
    private String surname;

    @CsvBindByPosition(position = 2)
    private String email;

    @CsvBindByPosition(position = 3)
    private String taxCode;

    @CsvBindByPosition(position = 4)
    private String status;

    @CsvBindByPosition(position = 5)
    private String institutionTaxCode;

    @CsvBindByPosition(position = 6)
    private String ptTaxCode;

    @CsvBindByPosition(position = 7)
    private String role;

}
