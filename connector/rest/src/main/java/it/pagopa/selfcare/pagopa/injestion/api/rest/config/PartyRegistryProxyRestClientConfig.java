package it.pagopa.selfcare.pagopa.injestion.api.rest.config;

import it.pagopa.selfcare.commons.connector.rest.config.RestClientBaseConfig;
import it.pagopa.selfcare.pagopa.injestion.api.rest.client.NationalRegistriesRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(RestClientBaseConfig.class)
@EnableFeignClients(clients = NationalRegistriesRestClient.class)
@PropertySource("classpath:config/party-registry-proxy-rest-client.properties")
public class PartyRegistryProxyRestClientConfig {
}
