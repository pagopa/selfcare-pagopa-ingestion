package it.pagopa.selfcare.pagopa.injestion.connector.dao.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

@Data
@NoArgsConstructor
@Document("ECIntermediarioPT")
@Sharded(shardKey = {"id"})
@FieldNameConstants(asEnum = true)
public class ECIntermediarioPTEntity {

    @Id
    private String id;
    private String enteIndirettoRagioneSociale;
    private String enteIndirettoCF;
    private String intermediarioPTRagioneSociale;
    private String intermediarioPTCF;

}
