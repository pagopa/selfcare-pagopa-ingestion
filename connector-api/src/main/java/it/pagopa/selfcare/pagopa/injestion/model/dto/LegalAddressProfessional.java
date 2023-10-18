package it.pagopa.selfcare.pagopa.injestion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LegalAddressProfessional {

    private String description;
    private String municipality;
    private String province;
    private String address;
    private String zip;

}
