package it.pagopa.selfcare.pagopa.ingestion.api.dao.repo;

import it.pagopa.selfcare.pagopa.ingestion.api.dao.model.ECPTRelationshipEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ECPTRelationshipRepository extends MongoRepository<ECPTRelationshipEntity, String>, MongoCustomConnector {

    @Query(value = "{'workStatus': ?0}")
    List<ECPTRelationshipEntity> findAllByStatus(String workStatus, Pageable pageable);

}
