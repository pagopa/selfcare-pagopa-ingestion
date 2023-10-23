package it.pagopa.selfcare.pagopa.injestion.api.dao.repo;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String>, MongoCustomConnector {

    @Query(value = "{'workStatus': ?0}")
    List<UserEntity> findAllByStatus(String workStatus, Pageable pageable);

    @Query(value = "{'institutionTaxCode': ?0}")
    List<UserEntity> findAllByInstitutionTaxCode(String institutionTaxCode);

}
