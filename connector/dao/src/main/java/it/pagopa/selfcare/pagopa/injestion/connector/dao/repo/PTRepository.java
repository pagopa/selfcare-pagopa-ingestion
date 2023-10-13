package it.pagopa.selfcare.pagopa.injestion.connector.dao.repo;

import it.pagopa.selfcare.pagopa.injestion.connector.dao.model.PTEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PTRepository extends MongoRepository<PTEntity, String>, MongoCustomConnector {
}
