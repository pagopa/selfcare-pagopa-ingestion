package it.pagopa.selfcare.pagopa.injestion.api.dao.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

@Data
@NoArgsConstructor
@Document("MigrazioneUser")
@Sharded(shardKey = {"id"})
@FieldNameConstants(asEnum = true)
public class UserEntity {

    @Id
    private String id;
    private String name;
    private String surname;
    private String email;
    private String taxCode;
    private String status;
    private String institutionTaxCode;
    private String role;
    private String workStatus;
    private int retry;

}
