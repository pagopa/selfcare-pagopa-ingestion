package it.pagopa.selfcare.pagopa.injestion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LegalAddress {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date dateTimeExtraction;

    private String taxId;

    private LegalAddressProfessional professionalAddress;

}
