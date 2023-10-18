package it.pagopa.selfcare.pagopa.injestion.core;

public interface MigrationPTService {

    void persistPT();

    void migratePT();

    void autoComplete();

}
