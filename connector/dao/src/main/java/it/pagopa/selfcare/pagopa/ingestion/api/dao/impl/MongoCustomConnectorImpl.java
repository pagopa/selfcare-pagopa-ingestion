package it.pagopa.selfcare.pagopa.ingestion.api.dao.impl;

import it.pagopa.selfcare.pagopa.ingestion.api.dao.repo.MongoCustomConnector;
import org.springframework.data.mongodb.core.MongoOperations;

public class MongoCustomConnectorImpl implements MongoCustomConnector {

    private final MongoOperations mongoOperations;

    public MongoCustomConnectorImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }


}
