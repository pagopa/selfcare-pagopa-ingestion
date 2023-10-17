package it.pagopa.selfcare.pagopa.injestion.api.dao.repo;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.ECEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;


import java.util.List;

@Repository
public interface ECRepository extends MongoRepository<ECEntity, String> {

    @Query(value = "{'workStatus': ?2}")
    List<ECEntity> findAllByStatus(String workStatus, Pageable pageable);

}
