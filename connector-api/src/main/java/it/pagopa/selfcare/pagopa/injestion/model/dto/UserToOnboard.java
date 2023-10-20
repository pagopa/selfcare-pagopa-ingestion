package it.pagopa.selfcare.pagopa.injestion.model.dto;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import lombok.Data;

@Data
public class UserToOnboard {
    private String taxCode;
    private PartyRole role;
    private String name;
    private String surname;
    private String email;
}
