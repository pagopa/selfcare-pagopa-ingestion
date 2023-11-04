package it.pagopa.selfcare.pagopa.ingestion.api.dao.model;

import it.pagopa.selfcare.commons.base.utils.Origin;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

@Data
@NoArgsConstructor
@Document("MigrazionePT")
@Sharded(shardKey = {"id"})
@FieldNameConstants(asEnum = true)
public class PTEntity {

    @Id
    private String id;
    private String businessName;
    private String taxCode;
    private String registeredOffice;
    private String zipCode;
    private String digitalAddress;
    private String vatNumber;
    private Origin origin;
    private String originId;
    private int onboardinHttpStatus;
    private String onboardingMessage;
    private String workStatus;

}
