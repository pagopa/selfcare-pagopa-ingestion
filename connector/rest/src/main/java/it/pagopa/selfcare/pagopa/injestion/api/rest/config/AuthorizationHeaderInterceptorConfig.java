package it.pagopa.selfcare.pagopa.injestion.api.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/authorization-header-interceptor.properties")
public class AuthorizationHeaderInterceptorConfig {
}