package it.pagopa.selfcare.pagopa.ingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SelfCarePagoPaIngestion{

    public static void main(String[] args) {
        SpringApplication.run(SelfCarePagoPaIngestion.class, args);
    }

}
