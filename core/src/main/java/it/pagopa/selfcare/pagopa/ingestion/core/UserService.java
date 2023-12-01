package it.pagopa.selfcare.pagopa.ingestion.core;

public interface UserService {

    void persistUser(String batchId);

    void migrateUser(String status);
}
