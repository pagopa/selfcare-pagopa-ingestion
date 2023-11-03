package it.pagopa.selfcare.pagopa.ingestion.api.mongo;

import it.pagopa.selfcare.pagopa.ingestion.model.dto.PT;

import java.util.List;

public interface PTConnector {

    List<PT> findAllByStatus(int page, int pageSize, String status);

    PT save(PT pt);

}
