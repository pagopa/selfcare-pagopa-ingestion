package it.pagopa.selfcare.pagopa.ingestion.core;

public interface ECService {

    void persistEC();

    void migrateEC(String status);

}
