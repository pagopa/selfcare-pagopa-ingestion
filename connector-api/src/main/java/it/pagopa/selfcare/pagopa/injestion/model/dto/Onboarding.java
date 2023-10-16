package it.pagopa.selfcare.pagopa.injestion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Onboarding {

    private BillingData billingData;
    private String institutionType;
    private List<GeographicTaxonomies> geographicTaxonomies;
    private Origin origin;
    private List<User> users;
    private AssistanceContracts assistanceContracts;
    private Status status;

}
