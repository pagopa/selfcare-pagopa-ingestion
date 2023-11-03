package it.pagopa.selfcare.pagopa.ingestion.api.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/feign-client.properties")
public class FeignClientConfig {
}
