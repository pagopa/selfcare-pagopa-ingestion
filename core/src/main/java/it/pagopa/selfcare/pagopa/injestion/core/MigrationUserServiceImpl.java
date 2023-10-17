package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.mapper.UserMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class MigrationUserServiceImpl implements MigrationUserService {

    private final MigrationService migrationService;
    private final UserConnector userConnector;
    private final String csvPath;

    public MigrationUserServiceImpl(
            MigrationService migrationService,
            UserConnector userConnector,
            @Value("${app.local.user}") String csvPath
    ) {
        this.migrationService = migrationService;
        this.userConnector = userConnector;
        this.csvPath = csvPath;
    }
    @Override
    public void persistUser() {
        migrationService.migrateEntities(UserModel.class, csvPath, userConnector::save, UserMapper::convertModelToDto);
    }
    

}
