package it.pagopa.selfcare.pagopa.injestion.api.rest.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import it.pagopa.selfcare.pagopa.injestion.exception.BadGatewayException;
import it.pagopa.selfcare.pagopa.injestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.injestion.exception.ServiceUnavailableException;

public class FeignErrorDecoder extends ErrorDecoder.Default{

    @Override
    public Exception decode(String methodKey, Response response) {
        if(response.status() == 503 || response.status() == 504){
            throw new ServiceUnavailableException();
        }
        if(response.status() == 500 || response.status() <= 509){
            throw new BadGatewayException(response.reason());
        }
        if(response.status() == 404){
            throw new ResourceNotFoundException(response.reason());
        }
        return super.decode(methodKey, response);
    }
}
