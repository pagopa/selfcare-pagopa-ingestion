package it.pagopa.selfcare.pagopa.ingestion.api.rest.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class ExternalInterceptorConfig {
    @Value("${authorization.external-api.subscriptionKey}")
    private String externalApiSubscriptionKey;

    @Bean
    public RequestInterceptor externalInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Ocp-Apim-Subscription-Key", externalApiSubscriptionKey);
        };
    }
}
