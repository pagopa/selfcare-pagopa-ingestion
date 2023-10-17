package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.PartyRegistryProxyRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.party_registry_proxy.InstitutionResponse;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.party_registry_proxy.LegalAddressResponse;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Institution;
import it.pagopa.selfcare.pagopa.injestion.model.dto.LegalAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public Institution findInstitution(String taxId, String origin, Optional<String> categories) {
        try {
            InstitutionResponse institutionResponse = partyRegistryProxyRestClient.findInstitution(taxId, origin, categories);
            return toInstitution(institutionResponse);
        } catch (Exception e) {
            log.error("Error while fetching institution data: {}", e.getMessage());
            return null;
        }
    }

    private Institution toInstitution(InstitutionResponse institutionResponse) {
        Institution institution = new Institution();
        institution.setId(institutionResponse.getId());
        institution.setTaxCode(institutionResponse.getTaxCode());
        institution.setCategory(institutionResponse.getCategory());
        institution.setDescription(institutionResponse.getDescription());
        institution.setDigitalAddress(institutionResponse.getDigitalAddress());
        institution.setAddress(institutionResponse.getAddress());
        institution.setZipCode(institutionResponse.getZipCode());
        return institution;
    }



}
