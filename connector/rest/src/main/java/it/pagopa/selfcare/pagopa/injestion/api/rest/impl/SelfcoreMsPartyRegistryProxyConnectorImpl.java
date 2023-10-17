package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.injestion.api.rest.SelfcareMsCoreConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.SelfcareMsPartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.SelfcareMsCoreRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.SelfcareMsPartyRegistryProxyRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_ms_core.InstitutionFromIpaPost;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_ms_core.InstitutionPaSubunitType;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_ms_core.InstitutionResponse;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_ms_party_registry_proxy.IniPecResponse;
import it.pagopa.selfcare.pagopa.injestion.model.dto.IniPec;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SelfcoreMsPartyRegistryProxyConnectorImpl implements SelfcareMsPartyRegistryProxyConnector {

    private final SelfcareMsPartyRegistryProxyRestClient selfcareMsPartyRegistryProxyRestClient;


    public SelfcoreMsPartyRegistryProxyConnectorImpl(SelfcareMsPartyRegistryProxyRestClient selfcareMsPartyRegistryProxyRestClient) {
        this.selfcareMsPartyRegistryProxyRestClient = selfcareMsPartyRegistryProxyRestClient;
    }


    @Override
    public IniPec getIniPec(String taxCode) {
        return toIniPec(selfcareMsPartyRegistryProxyRestClient.getInipec(taxCode));
    }

    private IniPec toIniPec(IniPecResponse iniPecResponse) {
        IniPec iniPec = new IniPec();
        iniPec.setAddress(iniPecResponse.getDigitalAddress());
        iniPec.setZipCode(iniPecResponse.getZipCode());
        return iniPec;
    }

}
