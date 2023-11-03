package it.pagopa.selfcare.pagopa.ingestion.api.mongo;

import it.pagopa.selfcare.pagopa.ingestion.model.dto.ECPTRelationship;

import java.util.List;

public interface ECPTRelationshipConnector {

    List<ECPTRelationship> findAllByStatus(int page, int pageSize, String status);

    ECPTRelationship save(ECPTRelationship ecptRelationship);

}
