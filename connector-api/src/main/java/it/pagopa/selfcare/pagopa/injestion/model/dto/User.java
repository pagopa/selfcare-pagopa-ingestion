package it.pagopa.selfcare.pagopa.injestion.model.dto;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    private String name;
    private String surname;
    private String taxCode;
    private Role role;
    private PartyRole partyRole;
    private String email;
    private Boolean status;
    private String institutionTaxCode;
    private String productRole;
    private WorkStatus workStatus;

}
