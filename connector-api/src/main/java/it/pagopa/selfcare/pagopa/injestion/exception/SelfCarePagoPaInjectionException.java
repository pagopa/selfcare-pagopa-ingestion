package it.pagopa.selfcare.pagopa.injestion.exception;

import lombok.Getter;

@Getter
public class SelfCarePagoPaInjectionException extends RuntimeException {

    private final String message;

    public SelfCarePagoPaInjectionException(String message){
        super(message);
        this.message = message;
    }

}
