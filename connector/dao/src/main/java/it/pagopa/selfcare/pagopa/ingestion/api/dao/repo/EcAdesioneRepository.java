package it.pagopa.selfcare.pagopa.ingestion.api.dao.repo;

import it.pagopa.selfcare.pagopa.ingestion.api.dao.model.EcAdesioneEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcAdesioneRepository extends MongoRepository<EcAdesioneEntity, String>, MongoCustomConnector {
    @Query(value = "{'workStatus': ?0}")
    List<EcAdesioneEntity> findAllByStatus(String workStatus, Pageable pageable);
}
