package it.pagopa.selfcare.pagopa.ingestion.api.dao.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

@Data
@NoArgsConstructor
@Document("User")
@Sharded(shardKey = {"correlationId"})
@FieldNameConstants(asEnum = true)
public class UserEntity {

    @Id
    private String correlationId;
    private String name;
    private String surname;
    private String email;
    private String taxCode;
    private String status;
    private String institutionTaxCode;
    private String ptTaxCode;
    private String role;
    private String workStatus;

}
