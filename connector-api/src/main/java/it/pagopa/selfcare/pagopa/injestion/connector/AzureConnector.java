package it.pagopa.selfcare.pagopa.injestion.connector;

import it.pagopa.selfcare.pagopa.injestion.dto.ResourceResponse;

public interface AzureConnector {

    ResourceResponse readCsv(String fileName);

}
