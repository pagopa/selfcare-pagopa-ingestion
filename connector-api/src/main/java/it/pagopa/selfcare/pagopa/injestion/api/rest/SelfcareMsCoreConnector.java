package it.pagopa.selfcare.pagopa.injestion.api.rest;

import it.pagopa.selfcare.pagopa.injestion.model.dto.Institution;

public interface SelfcareMsCoreConnector {

    Institution createInstitutionFromIpa(String taxCode, String subunitCode, String subunitType);

}
