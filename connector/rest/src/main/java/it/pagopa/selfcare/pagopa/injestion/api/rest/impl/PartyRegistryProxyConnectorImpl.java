package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.PartyRegistryProxyRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.LegalAddressResponse;
import it.pagopa.selfcare.pagopa.injestion.model.LegalAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PartyRegistryProxyConnectorImpl implements PartyRegistryProxyConnector {

    private final PartyRegistryProxyRestClient partyRegistryProxyRestClient;


    public PartyRegistryProxyConnectorImpl(PartyRegistryProxyRestClient partyRegistryProxyRestClient) {
        this.partyRegistryProxyRestClient = partyRegistryProxyRestClient;
    }

    @Override
    public LegalAddress getLegalAddress(String id) {
        return toLegalAddress(partyRegistryProxyRestClient.getLegalAddress(id));
    }

    private LegalAddress toLegalAddress(LegalAddressResponse legalAddressResponse) {
        LegalAddress legalAddress = new LegalAddress();
        legalAddress.setAddress(legalAddressResponse.getAddress());
        legalAddress.setZipCode(legalAddressResponse.getZipCode());
        return legalAddress;
    }

}
