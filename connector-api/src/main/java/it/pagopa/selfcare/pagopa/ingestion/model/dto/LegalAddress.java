package it.pagopa.selfcare.pagopa.ingestion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LegalAddress {

    private String address;
    private String zipCode;
}