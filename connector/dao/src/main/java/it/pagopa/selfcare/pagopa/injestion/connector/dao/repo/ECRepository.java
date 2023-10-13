package it.pagopa.selfcare.pagopa.injestion.connector.dao.repo;

import it.pagopa.selfcare.pagopa.injestion.connector.dao.model.ECEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ECRepository extends MongoRepository<ECEntity, String>, MongoCustomConnector {
}
