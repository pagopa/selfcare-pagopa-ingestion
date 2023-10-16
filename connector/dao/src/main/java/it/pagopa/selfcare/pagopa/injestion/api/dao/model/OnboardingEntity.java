package it.pagopa.selfcare.pagopa.injestion.api.dao.model;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.inner.AssistanceContractsEntity;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.inner.BillingDataEntity;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.inner.GeographicTaxonomiesEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

import java.util.List;

@Data
@NoArgsConstructor
@Document("Onboarding")
@Sharded(shardKey = {"id"})
@FieldNameConstants(asEnum = true)
public class OnboardingEntity {

    @Id
    private String id;

    private BillingDataEntity billingData;
    private String institutionType;
    private List<GeographicTaxonomiesEntity> geographicTaxonomies;
    private String origin;
    private List<UserEntity> users;
    private AssistanceContractsEntity assistanceContracts;
    private String status;
}
