package it.pagopa.selfcare.pagopa.ingestion.api.rest.client;

import it.pagopa.selfcare.pagopa.ingestion.api.rest.config.ExternalInterceptorConfig;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.InstitutionsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "${rest-client.external-api.serviceCode}", url = "${rest-client.external-api.base-url}", configuration = ExternalInterceptorConfig.class)
public interface ExternalApiRestClient {
    @GetMapping(value = "${rest-client.external-api.getInstitutions.path}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    InstitutionsResponse getInstitutions(@RequestParam(value = "taxCode") String taxCode);

}
