package it.pagopa.selfcare.pagopa.injestion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    private String name;
    private String surname;
    private String email;
    private String taxCode;
    private Boolean status;
    private String institutionTaxCode;
    private Role role;

}
