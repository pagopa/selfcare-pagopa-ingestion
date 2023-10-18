package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.injestion.api.rest.NationalRegistriesConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.NationalRegistriesRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.national_registries.GetAddressRegistroImpreseOKDto;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.national_registries.LegalAddressFilter;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.national_registries.LegalAddressRequest;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.national_registries.ProfessionalAddressDto;
import it.pagopa.selfcare.pagopa.injestion.model.dto.LegalAddress;
import it.pagopa.selfcare.pagopa.injestion.model.dto.LegalAddressProfessional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NationalRegistriesConnectorImpl implements NationalRegistriesConnector {

    private final NationalRegistriesRestClient nationalRegistriesRestClient;


    public NationalRegistriesConnectorImpl(NationalRegistriesRestClient nationalRegistriesRestClient) {
        this.nationalRegistriesRestClient = nationalRegistriesRestClient;
    }


    @Override
    public LegalAddress getLegalAddress(String taxCode) {
        LegalAddressRequest legalAddressRequest = createLegalAddressRequest(taxCode);
        return toLegalAddressResponse(nationalRegistriesRestClient.getLegalAddress(legalAddressRequest));
    }

    private LegalAddressRequest createLegalAddressRequest(String taxCode) {
        LegalAddressRequest legalAddressRequest = new LegalAddressRequest();
        LegalAddressFilter filter = new LegalAddressFilter();
        filter.setTaxId(taxCode);
        legalAddressRequest.setFilter(filter);
        return legalAddressRequest;
    }

    private LegalAddress toLegalAddressResponse(GetAddressRegistroImpreseOKDto legalAddress) {
        LegalAddress legalAddressResponse = new LegalAddress();
        legalAddressResponse.setTaxId(legalAddress.getTaxId());
        legalAddressResponse.setDateTimeExtraction(legalAddress.getDateTimeExtraction());
        if(legalAddress.getProfessionalAddress() != null) {
            legalAddressResponse.setProfessionalAddress(toProfessionalAddress(legalAddress.getProfessionalAddress()));
        }
        return legalAddressResponse;
    }

    private LegalAddressProfessional toProfessionalAddress(ProfessionalAddressDto professionalAddressDto){
        LegalAddressProfessional professionalAddress = new LegalAddressProfessional();
        professionalAddress.setAddress(professionalAddressDto.getAddress());
        professionalAddress.setMunicipality(professionalAddressDto.getMunicipality());
        professionalAddress.setZip(professionalAddressDto.getZip());
        professionalAddress.setProvince(professionalAddressDto.getProvince());
        professionalAddress.setDescription(professionalAddressDto.getDescription());
        return professionalAddress;
    }

}
