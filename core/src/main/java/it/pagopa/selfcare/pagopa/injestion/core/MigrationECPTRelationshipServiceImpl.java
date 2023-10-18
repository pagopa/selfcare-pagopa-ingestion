package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.pagopa.injestion.api.mongo.ECPTRelationshipConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.SelfcareExternalApiBackendConnector;
import it.pagopa.selfcare.pagopa.injestion.mapper.ECPTRelationshipMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.ECPTRelationshipModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Delegation;
import it.pagopa.selfcare.pagopa.injestion.model.dto.ECPTRelationship;
import it.pagopa.selfcare.pagopa.injestion.model.dto.WorkStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
class MigrationECPTRelationshipServiceImpl implements MigrationECPTRelationshipService {

    private final MigrationService migrationService;
    private final ECPTRelationshipConnector ecptRelationshipConnector;
    private final UserConnector userConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final SelfcareExternalApiBackendConnector selfcareExternalApiBackendConnector;

    @Value("${app.local.ec}")
    private String csvPath;

    @Value("${app.pageSize}")
    private int pageSize;

    public MigrationECPTRelationshipServiceImpl(
            MigrationService migrationService,
            ECPTRelationshipConnector ecptRelationshipConnector,
            UserConnector userConnector,
            PartyRegistryProxyConnector partyRegistryProxyConnector,
            SelfcareExternalApiBackendConnector selfcareExternalApiBackendConnector
    ) {
        this.migrationService = migrationService;
        this.ecptRelationshipConnector = ecptRelationshipConnector;
        this.userConnector = userConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.selfcareExternalApiBackendConnector = selfcareExternalApiBackendConnector;
    }
    @Override
    public void persistECPTRelationship() {
        migrationService.migrateEntities(ECPTRelationshipModel.class, csvPath, ecptRelationshipConnector::save, ECPTRelationshipMapper::convertModelToDto);
    }


    public void migrateECPTRelationship() {
        log.info("Starting migration of ECPTRelationship");

        int page = 0;

        while (true) {
            List<ECPTRelationship> ecptRelationships = ecptRelationshipConnector.findAllByStatus(page, pageSize, WorkStatus.NOT_WORKED.name());

            if (ecptRelationships.isEmpty()) {
                break;
            }

            ecptRelationships.forEach(this::migrateECPTRelationship);
            page++;
        }

        log.info("Completed migration of EC");
    }

    private void migrateECPTRelationship(ECPTRelationship ecptRelationship) {
        Delegation delegation = new Delegation();
        delegation.setFrom(ecptRelationship.getEnteIndirettoCF());
        delegation.setTo(ecptRelationship.getIntermediarioPTCF());
        delegation.setInstitutionFromName(ecptRelationship.getEnteIndirettoRagioneSociale());
        delegation.setInstitutionToName(ecptRelationship.getEnteIndirettoRagioneSociale());

    }
}
