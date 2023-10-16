package it.pagopa.selfcare.pagopa.injestion.api.dao.repo;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String>, MongoCustomConnector {
}
