package it.pagopa.selfcare.pagopa.ingestion.exception;

public class BadGatewayException extends RuntimeException {

    public BadGatewayException(String message) {
        super(message);
    }
}
