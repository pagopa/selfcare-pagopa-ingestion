package it.pagopa.selfcare.pagopa.injestion.core.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ECModel {

    @CsvBindByPosition(position = 0)
    private String businessName;

    @CsvBindByPosition(position = 1)
    private String taxCode;

    @CsvBindByPosition(position = 2)
    private String registeredOffice;

    @CsvBindByPosition(position = 3)
    private String zipCode;

    @CsvBindByPosition(position = 4)
    private String digitalAddress;

    @CsvBindByPosition(position = 5)
    private String vatNumber;

    @CsvBindByPosition(position = 6)
    private String recipientCode;

}
