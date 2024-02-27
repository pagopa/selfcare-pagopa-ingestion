package it.pagopa.selfcare.pagopa.ingestion.api.rest.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class InternalInterceptorConfig {
    @Value("${authorization.internal-api.subscriptionKey}")
    private String internalApiSubscriptionKey;

    @Bean
    public RequestInterceptor internalInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Ocp-Apim-Subscription-Key", internalApiSubscriptionKey);
        };
    }
}
