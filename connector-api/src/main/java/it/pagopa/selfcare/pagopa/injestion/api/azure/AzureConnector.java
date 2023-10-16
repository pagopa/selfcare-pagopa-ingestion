package it.pagopa.selfcare.pagopa.injestion.api.azure;

import it.pagopa.selfcare.pagopa.injestion.model.dto.Resource;

public interface AzureConnector {

    Resource readCsv(String fileName);

}
