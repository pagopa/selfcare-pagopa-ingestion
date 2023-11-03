package it.pagopa.selfcare.pagopa.ingestion.exception;

public class InternalException extends RuntimeException{

    public InternalException(String message) {
        super(message);
    }

    public InternalException() {
        super();
    }


}
