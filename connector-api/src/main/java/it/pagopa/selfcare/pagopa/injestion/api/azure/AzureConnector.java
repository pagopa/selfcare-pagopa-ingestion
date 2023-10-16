package it.pagopa.selfcare.pagopa.injestion.api.azure;

import it.pagopa.selfcare.pagopa.injestion.model.Resource;

public interface AzureConnector {

    Resource readCsv(String fileName);

}
