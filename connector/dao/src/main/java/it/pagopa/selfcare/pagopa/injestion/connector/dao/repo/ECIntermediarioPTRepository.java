package it.pagopa.selfcare.pagopa.injestion.connector.dao.repo;

import it.pagopa.selfcare.pagopa.injestion.connector.dao.model.ECIntermediarioPTEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ECIntermediarioPTRepository extends MongoRepository<ECIntermediarioPTEntity, String>, MongoCustomConnector {
}
