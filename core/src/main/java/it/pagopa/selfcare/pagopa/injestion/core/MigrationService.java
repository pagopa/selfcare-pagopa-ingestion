package it.pagopa.selfcare.pagopa.injestion.core;

public interface MigrationService {

    void migrateECIntermediarioPTs();

    void migrateECs();

    void migratePTs();

    void migrateUsers();

    void migrateEC();

}
