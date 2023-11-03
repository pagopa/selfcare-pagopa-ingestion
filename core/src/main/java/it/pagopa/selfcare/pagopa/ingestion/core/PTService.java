package it.pagopa.selfcare.pagopa.ingestion.core;

public interface PTService {

    void persistPT();

    void migratePT(String status);


}
