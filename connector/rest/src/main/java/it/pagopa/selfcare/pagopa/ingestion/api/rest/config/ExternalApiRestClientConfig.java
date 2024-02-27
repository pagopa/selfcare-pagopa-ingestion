package it.pagopa.selfcare.pagopa.ingestion.api.rest.config;

import feign.RequestInterceptor;
import it.pagopa.selfcare.commons.connector.rest.config.RestClientBaseConfig;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.client.ExternalApiRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(RestClientBaseConfig.class)
@EnableFeignClients(clients = ExternalApiRestClient.class)
@PropertySource("classpath:config/external-api-rest-client.properties")
public class ExternalApiRestClientConfig {
    @Value("${authorization.external-api.subscriptionKey}")
    private String externalApiSubscriptionKey;

    @Bean
    public RequestInterceptor externalInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Ocp-Apim-Subscription-Key", externalApiSubscriptionKey);
        };
    }
}
