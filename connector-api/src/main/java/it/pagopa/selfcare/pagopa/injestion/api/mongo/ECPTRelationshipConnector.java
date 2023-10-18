package it.pagopa.selfcare.pagopa.injestion.api.mongo;

import it.pagopa.selfcare.pagopa.injestion.model.dto.ECPTRelationship;

import java.util.List;

public interface ECPTRelationshipConnector {

    List<ECPTRelationship> findAll(int page, int pageSize);

    List<ECPTRelationship> findAll();

    List<ECPTRelationship> findAllByStatus(int page, int pageSize, String status);

    ECPTRelationship findById(String id);

    ECPTRelationship save(ECPTRelationship ecptRelationship);

    List<ECPTRelationship> saveAll(List<ECPTRelationship> ecptRelationshipList);

    void deleteById(String id);

    void delete(ECPTRelationship ecptRelationship);

}
