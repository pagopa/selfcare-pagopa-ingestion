package it.pagopa.selfcare.pagopa.ingestion.exception;

public class PartyRegistryProxyException extends RuntimeException {

    private final String code;

    public PartyRegistryProxyException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
