package it.pagopa.selfcare.pagopa.ingestion.core;

import feign.FeignException;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.ECPTRelationshipConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.ingestion.mapper.ECPTRelationshipMapper;
import it.pagopa.selfcare.pagopa.ingestion.model.csv.ECPTRelationshipModel;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.Delegation;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.ECPTRelationship;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static it.pagopa.selfcare.pagopa.ingestion.core.util.MigrationUtil.createDelegation;

@Service
@Slf4j
class DelegationServiceImpl implements DelegationService {

    private final MigrationService migrationService;
    private final ECPTRelationshipConnector ecptRelationshipConnector;
    private final InternalApiConnector internalApiConnector;

    @Value("${app.local.ecptrelationship}")
    private String csvPath;

    @Value("${app.pageSize}")
    private int pageSize;

    public DelegationServiceImpl(
            MigrationService migrationService,
            ECPTRelationshipConnector ecptRelationshipConnector,
            InternalApiConnector internalApiConnector) {
        this.migrationService = migrationService;
        this.ecptRelationshipConnector = ecptRelationshipConnector;
        this.internalApiConnector = internalApiConnector;
    }
    @Override
    @Async
    public void persistECPTRelationship() {
        migrationService.migrateEntities(ECPTRelationshipModel.class, csvPath, ecptRelationshipConnector::save, ECPTRelationshipMapper::convertModelToDto);
    }


    @Override
    @Async
    public void migrateECPTRelationship(String status) {
        log.info("Starting migration of ECPTRelationship");
        int page = 0;
        boolean hasNext = true;
        do {
            List<ECPTRelationship> ecptRelationships = ecptRelationshipConnector.findAllByStatus(page, pageSize, status);
            if (!CollectionUtils.isEmpty(ecptRelationships)) {
                ecptRelationships.forEach(this::migrateECPTRelationship);
            } else {
                hasNext = false;
            }
        } while (Boolean.TRUE.equals(hasNext));
        log.info("Completed migration of ECPTRelationship");
    }

    private void migrateECPTRelationship(ECPTRelationship ecptRelationship) {
        Delegation delegation = createDelegation(ecptRelationship);
        try {
            internalApiConnector.createDelegation(delegation);
            ecptRelationship.setWorkStatus(WorkStatus.DONE);
            ecptRelationship.setCreateHttpStatus(200);
        }catch (FeignException e){
            ecptRelationship.setWorkStatus(WorkStatus.ERROR);
            ecptRelationship.setCreateHttpStatus(e.status());
            ecptRelationship.setCreateMessage(e.getMessage());
        }
        ecptRelationshipConnector.save(ecptRelationship);
    }
}
