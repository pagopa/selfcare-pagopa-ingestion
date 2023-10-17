package it.pagopa.selfcare.pagopa.injestion.api.dao.model;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

@Data
@Document("MigrazioneEC")
@Sharded(shardKey = {"id"})
@FieldNameConstants(asEnum = true)
public class ECEntity {

    @Id
    private String id;
    private String businessName;
    private String taxCode;
    private String registeredOffice;
    private String zipCode;
    private String digitalAddress;
    private String vatNumber;
    private String recipientCode;
    private String workStatus;

}
