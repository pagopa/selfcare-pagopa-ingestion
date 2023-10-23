package it.pagopa.selfcare.pagopa.injestion.api.mongo;

import it.pagopa.selfcare.pagopa.injestion.model.dto.EC;

import java.util.List;

public interface ECConnector {

    List<EC> findAllByStatus(int page, int pageSize, String status);

    EC save(EC ec);
}
