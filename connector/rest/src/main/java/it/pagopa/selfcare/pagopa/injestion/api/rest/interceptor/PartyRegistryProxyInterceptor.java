package it.pagopa.selfcare.pagopa.injestion.api.rest.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartyRegistryProxyInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.trace("apply start");
        log.debug("RequestTemplate: {}",requestTemplate);
        log.trace("apply end");
    }
}
