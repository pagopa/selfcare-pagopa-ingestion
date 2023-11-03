package it.pagopa.selfcare.pagopa.ingestion.api.mongo;

import it.pagopa.selfcare.pagopa.ingestion.model.dto.EC;

import java.util.List;

public interface ECConnector {

    List<EC> findAllByStatus(int page, int pageSize, String status);

    EC save(EC ec);
}
