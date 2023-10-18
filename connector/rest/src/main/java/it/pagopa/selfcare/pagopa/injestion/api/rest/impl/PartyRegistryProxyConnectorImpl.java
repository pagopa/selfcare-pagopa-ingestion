package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import feign.FeignException;
import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.PartyRegistryProxyRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.party_registry_proxy.ProxyInstitutionResponse;
import it.pagopa.selfcare.pagopa.injestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.injestion.exception.SelfCarePagoPaInjectionException;
import it.pagopa.selfcare.pagopa.injestion.model.dto.InstitutionProxyInfo;
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
    public InstitutionProxyInfo getInstitutionById(String id) {
        try {
            ProxyInstitutionResponse response = partyRegistryProxyRestClient.getInstitutionById(id);
            if (response == null) {
                throw new ResourceNotFoundException(String.format("INSTITUTION NOT FOUND: %s", id));
            }
            return convertInstitutionProxyInfo(response);
        } catch (FeignException e) {
            throw new SelfCarePagoPaInjectionException(e.getMessage(), String.valueOf(e.status()));
        }
    }

    private InstitutionProxyInfo convertInstitutionProxyInfo(ProxyInstitutionResponse response) {
        InstitutionProxyInfo info = new InstitutionProxyInfo();
        info.setId(response.getId());
        info.setOriginId(response.getOriginId());
        info.setO(response.getO());
        info.setOu(response.getOu());
        info.setAoo(response.getAoo());
        info.setTaxCode(response.getTaxCode());
        info.setCategory(response.getCategory());
        info.setDescription(response.getDescription());
        info.setDigitalAddress(response.getDigitalAddress());
        info.setAddress(response.getAddress());
        info.setZipCode(response.getZipCode());
        info.setOrigin(response.getOrigin());
        info.setIstatCode(response.getIstatCode());
        return info;
    }

}
