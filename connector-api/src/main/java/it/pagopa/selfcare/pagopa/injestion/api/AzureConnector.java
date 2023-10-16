package it.pagopa.selfcare.pagopa.injestion.api;

import it.pagopa.selfcare.pagopa.injestion.dto.ResourceResponse;

public interface AzureConnector {

    ResourceResponse readCsv(String fileName);

}
