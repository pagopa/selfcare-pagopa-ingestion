package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.injestion.api.rest.ExternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.ExternalApiRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.external.DelegationRequest;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Delegation;

public class ExternalApiConnectorImpl implements ExternalApiConnector {

    private final ExternalApiRestClient externalApiRestClient;

    public ExternalApiConnectorImpl(ExternalApiRestClient externalApiRestClient) {
        this.externalApiRestClient = externalApiRestClient;
    }

    @Override
    public void createDelegation(Delegation delegation){
        externalApiRestClient.createDelegation(toDelegationRequest(delegation));
    }

    private DelegationRequest toDelegationRequest(Delegation delegation){
        DelegationRequest delegationRequest = new DelegationRequest();
        delegationRequest.setTo(delegation.getTo());
        delegationRequest.setFrom(delegation.getFrom());
        delegationRequest.setInstitutionToName(delegation.getInstitutionToName());
        delegationRequest.setInstitutionFromName(delegation.getInstitutionFromName());
        delegationRequest.setType(delegation.getType());
        delegationRequest.setProductId(delegation.getProductId());
        return delegationRequest;
    }
}
