package it.pagopa.selfcare.pagopa.ingestion.core;

public interface DelegationService {

    void persistECPTRelationship();

    void migrateECPTRelationship();

}
