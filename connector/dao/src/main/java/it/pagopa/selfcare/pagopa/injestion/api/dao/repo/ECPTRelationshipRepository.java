package it.pagopa.selfcare.pagopa.injestion.api.dao.repo;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.ECPTRelationshipEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ECPTRelationshipRepository extends MongoRepository<ECPTRelationshipEntity, String>, MongoCustomConnector {
}
