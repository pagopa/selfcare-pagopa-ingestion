package it.pagopa.selfcare.pagopa.injestion.core;

public interface MigrationService {

    void migrateECIntermediarioPTs(String ecIntermediarioPTPath);

    void migrateECs(String ecPath);

    void migratePTs(String ptPath);

    void migrateUsers(String usersPath);

}
