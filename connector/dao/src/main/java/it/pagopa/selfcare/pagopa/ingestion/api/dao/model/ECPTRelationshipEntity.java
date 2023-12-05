package it.pagopa.selfcare.pagopa.ingestion.api.dao.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

@Data
@NoArgsConstructor
@Document("Delegation")
@Sharded(shardKey = {"correlationId"})
@FieldNameConstants(asEnum = true)
public class ECPTRelationshipEntity {

    @Id
    private String correlationId;
    private String enteIndirettoRagioneSociale;
    private String enteIndirettoCF;
    private String intermediarioPTRagioneSociale;
    private String intermediarioPTCF;
    private String workStatus;
    private int createHttpStatus;
    private String createMessage;
    private String batchId;

}
