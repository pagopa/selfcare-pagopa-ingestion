package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.pagopa.injestion.api.mongo.PTConnector;
import it.pagopa.selfcare.pagopa.injestion.mapper.PTMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.PTModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class MigrationPTServiceImpl implements MigrationPTService {

    private final MigrationService migrationService;
    private final PTConnector ptConnector;
    private final String csvPath;

    public MigrationPTServiceImpl(
            MigrationService migrationService,
            PTConnector ptConnector,
            @Value("${app.local.pt}") String csvPath
    ) {
        this.migrationService = migrationService;
        this.ptConnector = ptConnector;
        this.csvPath = csvPath;
    }
    @Override
    public void persistPT() {
        migrationService.migrateEntities(PTModel.class, csvPath, ptConnector::save, PTMapper::convertModelToDto);
    }
    

}
