package it.pagopa.selfcare.pagopa.injestion.core;

public interface UserService {

    void persistUser();

    void migrateUser(String status);
}
