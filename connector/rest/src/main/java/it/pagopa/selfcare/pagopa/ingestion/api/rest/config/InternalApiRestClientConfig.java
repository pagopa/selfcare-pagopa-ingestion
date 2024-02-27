package it.pagopa.selfcare.pagopa.ingestion.api.rest.config;

import feign.RequestInterceptor;
import it.pagopa.selfcare.commons.connector.rest.config.RestClientBaseConfig;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.client.InternalApiRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(RestClientBaseConfig.class)
@EnableFeignClients(clients = InternalApiRestClient.class)
@PropertySource("classpath:config/internal-api-rest-client.properties")
public class InternalApiRestClientConfig {
    /*
    @Value("${authorization.internal-api.subscriptionKey}")
    private String internalApiSubscriptionKey;

    @Bean
    public RequestInterceptor internalInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Ocp-Apim-Subscription-Key", internalApiSubscriptionKey);
        };
    }

     */
}
