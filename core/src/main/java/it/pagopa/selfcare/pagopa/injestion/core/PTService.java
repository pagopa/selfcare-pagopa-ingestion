package it.pagopa.selfcare.pagopa.injestion.core;

public interface PTService {

    void persistPT();

    void migratePT(String status);


}
