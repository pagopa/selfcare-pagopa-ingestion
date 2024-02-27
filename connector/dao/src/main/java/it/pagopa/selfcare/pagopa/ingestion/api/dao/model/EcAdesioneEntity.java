package it.pagopa.selfcare.pagopa.ingestion.api.dao.model;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Document("ECAdesione")
@Sharded(shardKey = {"id"})
@FieldNameConstants(asEnum = true)
public class EcAdesioneEntity {
    @Id
    private String id;
    private String taxCode;
    private String businessName;
    private String dateOfJoining;
    private String institutionId;
    private String workStatus;
    private Integer httpStatus;
    private String httpMessage;
    private String batchId;
}
