package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.pagopa.injestion.api.mongo.ECPTRelationshipConnector;
import it.pagopa.selfcare.pagopa.injestion.mapper.ECPTRelationshipMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.ECPTRelationshipModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class MigrationECPTRelationshipServiceImpl implements MigrationECPTRelationshipService {

    private final MigrationService migrationService;
    private final ECPTRelationshipConnector ecptRelationshipConnector;
    private final String csvPath;

    public MigrationECPTRelationshipServiceImpl(
            MigrationService migrationService,
            ECPTRelationshipConnector ecptRelationshipConnector,
            @Value("${app.local.ecptrelationship}") String csvPath
    ) {
        this.migrationService = migrationService;
        this.ecptRelationshipConnector = ecptRelationshipConnector;
        this.csvPath = csvPath;
    }
    @Override
    public void persistECPTRelationship() {
        migrationService.migrateEntities(ECPTRelationshipModel.class, csvPath, ecptRelationshipConnector::save, ECPTRelationshipMapper::convertModelToDto);
    }
    

}
