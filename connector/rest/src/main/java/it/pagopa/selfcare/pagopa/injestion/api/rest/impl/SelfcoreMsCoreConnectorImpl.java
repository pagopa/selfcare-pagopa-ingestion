package it.pagopa.selfcare.pagopa.injestion.api.rest.impl;

import it.pagopa.selfcare.pagopa.injestion.api.rest.SelfcareMsCoreConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.SelfcareMsCoreRestClient;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_ms_core.InstitutionFromIpaPost;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_ms_core.InstitutionPaSubunitType;
import it.pagopa.selfcare.pagopa.injestion.api.rest.model.selfcare_ms_core.InstitutionResponse;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SelfcoreMsCoreConnectorImpl implements SelfcareMsCoreConnector {

    private final SelfcareMsCoreRestClient selfcareMsCoreRestClient;


    public SelfcoreMsCoreConnectorImpl(SelfcareMsCoreRestClient selfcareMsCoreRestClient) {
        this.selfcareMsCoreRestClient = selfcareMsCoreRestClient;
    }

    @Override
    public Institution createInstitutionFromIpa(String taxCode, String subunitCode, String subunitType) {
        log.info("createInstitution");
        return toInstitution(selfcareMsCoreRestClient.createInstitutionFromIpa(toInstitutionFromIpaPost(taxCode, subunitCode, subunitType)));
    }

    private InstitutionFromIpaPost toInstitutionFromIpaPost(String taxCode, String subunitCode, String subunitType) {
        InstitutionFromIpaPost institutionFromIpaPost = new InstitutionFromIpaPost();
        institutionFromIpaPost.setTaxCode(taxCode);
        institutionFromIpaPost.setSubunitCode(subunitCode);
        institutionFromIpaPost.setSubunitType(subunitType == null ? null : InstitutionPaSubunitType.valueOf(subunitType));
        return institutionFromIpaPost;
    }

    private Institution toInstitution(InstitutionResponse institutionResponse){

        Institution institution = new Institution();
        institution.setId(institutionResponse.getId());
        institution.setOriginId(institutionResponse.getOriginId());
        institution.setOu(institutionResponse.getOu());
        institution.setO(institutionResponse.getO());
        institution.setAoo(institutionResponse.getAoo());
        institution.setTaxCode(institutionResponse.getTaxCode());
        institution.setAddress(institutionResponse.getAddress());
        institution.setZipCode(institutionResponse.getZipCode());
        institution.setCategory(institutionResponse.getCategory());
        institution.setDescription(institutionResponse.getDescription());
        institution.setDigitalAddress(institutionResponse.getDigitalAddress());
        institution.setOrigin(institutionResponse.getOrigin());
        institution.setIstatCode(institutionResponse.getIstatCode());

        return institution;
    }


}
