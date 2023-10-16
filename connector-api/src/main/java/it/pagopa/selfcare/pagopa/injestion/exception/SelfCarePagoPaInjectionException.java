package it.pagopa.selfcare.pagopa.injestion.exception;

import lombok.Getter;

@Getter
public class SelfCarePagoPaInjectionException extends RuntimeException {

    private final String code;

    public SelfCarePagoPaInjectionException(String message, String code){
        super(message);
        this.code = code;
    }

}
