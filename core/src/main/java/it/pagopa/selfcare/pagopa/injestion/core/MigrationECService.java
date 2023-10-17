package it.pagopa.selfcare.pagopa.injestion.core;

public interface MigrationECService {

    void persistEC();

    void migrateEC();

    void autoComplete();

}
