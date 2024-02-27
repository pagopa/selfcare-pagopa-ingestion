package it.pagopa.selfcare.pagopa.ingestion.core.exception;

public class TooManyInstitutionsException extends RuntimeException {
    public TooManyInstitutionsException(String message) {
        super(message);
    }
}
