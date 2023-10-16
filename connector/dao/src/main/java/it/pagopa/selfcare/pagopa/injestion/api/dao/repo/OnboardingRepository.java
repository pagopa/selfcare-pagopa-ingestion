package it.pagopa.selfcare.pagopa.injestion.api.dao.repo;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.OnboardingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardingRepository extends MongoRepository<OnboardingEntity, String>, MongoCustomConnector {
}
