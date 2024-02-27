package it.pagopa.selfcare.pagopa.ingestion.api.mongo;

import it.pagopa.selfcare.pagopa.ingestion.model.dto.EcAdesione;

import java.util.List;

public interface EcAdesioneConnector {
    EcAdesione save(EcAdesione ecAdesione);

    List<EcAdesione> findAllByStatus(int page, int pageSize, String status);

}
