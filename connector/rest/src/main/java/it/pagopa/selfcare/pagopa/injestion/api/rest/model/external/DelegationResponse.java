package it.pagopa.selfcare.pagopa.injestion.api.rest.model.external;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import lombok.Data;

@Data
public class DelegationResponse {

    private String id;
    private String institutionId;
    private String institutionName;
    private String institutionRootName;
    private DelegationType type;
    private String productId;
    private String taxCode;
    private InstitutionType institutionType;
    private String brokerId;
    private String brokerTaxCode;
    private String brokerType;
    private String brokerName;

}
