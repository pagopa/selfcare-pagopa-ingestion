package it.pagopa.selfcare.pagopa.ingestion.core;

public interface ECService {

    void persistEC(String batchId);

    void migrateEC(String status);

    void persistECAdesione(String batchId);

    void migrateECAdesione(String status);

}
