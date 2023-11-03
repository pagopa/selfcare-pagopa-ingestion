package it.pagopa.selfcare.pagopa.ingestion.exception;

public class InvalidRequestException extends RuntimeException  {

    public InvalidRequestException(String message){
        super(message);
    }

}
