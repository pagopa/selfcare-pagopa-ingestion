package it.pagopa.selfcare.pagopa.ingestion.model.dto;

import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    private String correlationId;
    private String name;
    private String surname;
    private String taxCode;
    private Role role;
    private String email;
    private String status;
    private String institutionTaxCode;
    private String productRole;
    private WorkStatus workStatus;
    private Integer onboardingHttpStatus;
    private String onboardingMessage;
}
