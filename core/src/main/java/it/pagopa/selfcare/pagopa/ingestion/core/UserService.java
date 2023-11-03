package it.pagopa.selfcare.pagopa.ingestion.core;

public interface UserService {

    void persistUser();

    void migrateUser(String status);
}
