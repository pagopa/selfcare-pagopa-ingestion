package it.pagopa.selfcare.pagopa.ingestion.api.rest.model.external;

import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class CreatedAtRequest {
    private String productId;
    private String createdAt;
    private String activatedAt;
}
