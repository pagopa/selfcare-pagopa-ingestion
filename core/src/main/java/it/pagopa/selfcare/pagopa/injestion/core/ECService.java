package it.pagopa.selfcare.pagopa.injestion.core;

public interface ECService {

    void persistEC();

    void migrateEC(String status);

}
