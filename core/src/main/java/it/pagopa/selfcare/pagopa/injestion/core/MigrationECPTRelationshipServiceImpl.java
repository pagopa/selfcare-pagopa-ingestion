package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.pagopa.injestion.api.mongo.ECPTRelationshipConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.ExternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.injestion.mapper.ECPTRelationshipMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.ECPTRelationshipModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Delegation;
import it.pagopa.selfcare.pagopa.injestion.model.dto.ECPTRelationship;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
class MigrationECPTRelationshipServiceImpl implements MigrationECPTRelationshipService {

    private final MigrationService migrationService;
    private final ECPTRelationshipConnector ecptRelationshipConnector;
    private final ExternalApiConnector externalApiConnector;

    @Value("${app.local.ec}")
    private String csvPath;

    @Value("${app.pageSize}")
    private int pageSize;

    public MigrationECPTRelationshipServiceImpl(
            MigrationService migrationService,
            ECPTRelationshipConnector ecptRelationshipConnector,
            ExternalApiConnector externalApiConnector) {
        this.migrationService = migrationService;
        this.ecptRelationshipConnector = ecptRelationshipConnector;
        this.externalApiConnector = externalApiConnector;
    }
    @Override
    public void persistECPTRelationship() {
        migrationService.migrateEntities(ECPTRelationshipModel.class, csvPath, ecptRelationshipConnector::save, ECPTRelationshipMapper::convertModelToDto);
    }


    @Override
    public void migrateECPTRelationship() {
        log.info("Starting migration of ECPTRelationship");

        int page = 0;

        while (true) {

            List<ECPTRelationship> ecptRelationships = ecptRelationshipConnector.findAllByStatus(page, pageSize, WorkStatus.NOT_WORKED.name());

            if (ecptRelationships.isEmpty()) {
                break;
            }

            ecptRelationships.forEach(this::migrateECPTRelationship);

        }

        log.info("Completed migration of EC");
    }

    private void migrateECPTRelationship(ECPTRelationship ecptRelationship) {
        Delegation delegation = new Delegation();
        delegation.setFrom(ecptRelationship.getEnteIndirettoCF());
        delegation.setTo(ecptRelationship.getIntermediarioPTCF());
        delegation.setInstitutionFromName(ecptRelationship.getEnteIndirettoRagioneSociale());
        delegation.setInstitutionToName(ecptRelationship.getEnteIndirettoRagioneSociale());
        externalApiConnector.createDelegation(delegation);
        ecptRelationship.setWorkStatus(WorkStatus.DONE);
        ecptRelationshipConnector.save(ecptRelationship);
    }
}
