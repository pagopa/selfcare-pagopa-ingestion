package it.pagopa.selfcare.pagopa.ingestion.core;

import feign.FeignException;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.ECConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.EcAdesioneConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.ExternalApiConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.ingestion.core.exception.TooManyInstitutionsException;
import it.pagopa.selfcare.pagopa.ingestion.core.util.ParallelUtil;
import it.pagopa.selfcare.pagopa.ingestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.ingestion.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.ingestion.model.csv.ECAdesioneModel;
import it.pagopa.selfcare.pagopa.ingestion.model.csv.ECModel;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static it.pagopa.selfcare.pagopa.ingestion.core.util.MigrationUtil.*;

@Service
@Slf4j
class ECServiceImpl implements ECService {

    private final MigrationService migrationService;
    private final ECConnector ecConnector;
    private final EcAdesioneConnector ecAdesioneConnector;
    private final UserConnector userConnector;
    private final InternalApiConnector internalApiConnector;
    private final ExternalApiConnector externalApiConnector;
    private final String ecCsvPath;

    private final String ecAdesioneCsvPath;
    private final int pageSize;

    public ECServiceImpl(
            MigrationService migrationService,
            ECConnector ecConnector,
            EcAdesioneConnector ecAdesioneConnector, UserConnector userConnector,
            InternalApiConnector internalApiConnector,
            ExternalApiConnector externalApiConnector, @Value("${app.pageSize}") int pageSize,
            @Value("${app.local.ec}") String ecCsvPath,
            @Value("${app.local.ecAdesione}") String ecAdesioneCsvPath) {
        this.migrationService = migrationService;
        this.ecConnector = ecConnector;
        this.ecAdesioneConnector = ecAdesioneConnector;
        this.userConnector = userConnector;
        this.internalApiConnector = internalApiConnector;
        this.externalApiConnector = externalApiConnector;
        this.pageSize = pageSize;
        this.ecCsvPath = ecCsvPath;
        this.ecAdesioneCsvPath = ecAdesioneCsvPath;
    }

    @Override
    @Async
    public void persistEC(String batchId) {
        if(StringUtils.hasText(batchId)) {
            migrationService.migrateEntitiesWithBatchId(ECModel.class, ecCsvPath, ecConnector::save, ECMapper::convertModelToDtoWithBatchId, batchId);
        } else {
            migrationService.migrateEntities(ECModel.class, ecCsvPath, ecConnector::save, ECMapper::convertModelToDto);
        }
    }

    @Override
    @Async
    public void migrateEC(String status) {
        log.info("Starting migration of EC");
        int page = 0;
        boolean hasNext = true;
        do {
            List<EC> ecList = ecConnector.findAllByStatus(page, pageSize, status);
            if (!CollectionUtils.isEmpty(ecList)) {
                //ParallelUtil.runParallel(1, () -> ecList.parallelStream().forEach(this::onboardEc));
                ecList.forEach(this::onboardEc);
            } else {
                hasNext = false;
            }
        } while (Boolean.TRUE.equals(hasNext));

        log.info("Completed migration of EC");
    }

    private void onboardEc(EC ec) {
        try {
            User user = userConnector.findManagerByInstitutionTaxCodeAndRole(ec.getTaxCode(), Role.RP);
            if(!user.getTaxCode().equalsIgnoreCase("NO_TAXCODE")){
                AutoApprovalOnboarding onboarding = constructOnboardingDto(ec, List.of(user));
                processMigrateEC(ec, onboarding, user);
            }else{
                ec.setWorkStatus(WorkStatus.EMPTY_MANAGER_CF);
                ecConnector.save(ec);
            }
        } catch (ResourceNotFoundException e) {
            ec.setWorkStatus(WorkStatus.MANAGER_NOT_FOUND);
            ecConnector.save(ec);
        }
    }

    private void processMigrateEC(EC ec, AutoApprovalOnboarding onboarding, User user) {
        try {
            internalApiConnector.autoApprovalOnboarding("EC", onboarding);
            ec.setWorkStatus(WorkStatus.DONE);
            ec.setOnboardingHttpStatus(200);
            user.setWorkStatus(WorkStatus.DONE);
        } catch (FeignException e) {
            if (e.status() == 404) {
                log.error("Error while migrating EC: TaxCode {} not found in registry", ec.getTaxCode(), e);
                ec.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY);
                user.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY);
            } else if (e.status() == 409) {
                log.error("Error while migrating EC: product already onboarded for TaxCode {}", ec.getTaxCode(), e);
                ec.setWorkStatus(WorkStatus.ALREADY_ONBOARDED);
                ec.setOnboardingHttpStatus(e.status());
                ec.setOnboardingMessage(e.getMessage());
                user.setWorkStatus(WorkStatus.NOT_WORKED);
            } else {
                log.error("Error while migrating EC for tax code: " + ec.getTaxCode(), e);
                ec.setWorkStatus(WorkStatus.ERROR);
                ec.setOnboardingHttpStatus(e.status());
                ec.setOnboardingMessage(e.getMessage());
                user.setWorkStatus(WorkStatus.ERROR);
            }
        } finally {
            ecConnector.save(ec);
            userConnector.save(user);
        }
    }

    @Override
    @Async
    public void persistECAdesione(String batchId) {
        if(StringUtils.hasText(batchId)) {
            migrationService.migrateEntitiesWithBatchId(ECAdesioneModel.class, ecAdesioneCsvPath, ecAdesioneConnector::save, ECMapper::convertModelToDtoWithBatchId, batchId);
        } else {
            migrationService.migrateEntities(ECAdesioneModel.class, ecAdesioneCsvPath, ecAdesioneConnector::save, ECMapper::convertModelToDto);
        }
    }

    @Override
    public void migrateECAdesione(String status) {
        log.info("Starting EcAdesione migration");
        int page = 0;
       /* boolean hasNext = true;
        do {
            List<EcAdesione> ecList = ecAdesioneConnector.findAllByStatus(page, pageSize, status);
            if (!CollectionUtils.isEmpty(ecList)) {
                ParallelUtil.runParallel(3, () -> ecList.parallelStream().forEach(this::workOnEcAdesione));
            } else {
                hasNext = false;
            }
        } while (Boolean.TRUE.equals(hasNext));


        */
        List<EcAdesione> ecList = ecAdesioneConnector.findAllByStatus(page, 50, status);
        if (!CollectionUtils.isEmpty(ecList)) {
            ParallelUtil.runParallel(3, () -> ecList.parallelStream().forEach(this::workOnEcAdesione));
        }

        log.info("Completed migration of EC");
    }

    private void workOnEcAdesione(EcAdesione ecAdesione) {
        if(!StringUtils.hasText(ecAdesione.getInstitutionId())) {
            getInstitutionId(ecAdesione);
        }
        if(StringUtils.hasText(ecAdesione.getInstitutionId())) {
            updateJoiningDate(ecAdesione);
        }
    }

    private void getInstitutionId(EcAdesione ecAdesione) {
        try {
            log.info("Getting institutionId for taxCode: {}", ecAdesione.getTaxCode());
            InstitutionsResponse institutionsResponse = externalApiConnector.getInstitutions(ecAdesione.getTaxCode());
            if(institutionsResponse.getInstitutions().size() > 1) {
                throw new TooManyInstitutionsException("Too many institutions found for taxCode: " + ecAdesione.getTaxCode());
            }

            String institutionId = institutionsResponse.getInstitutions().get(0).getId();
            ecAdesione.setInstitutionId(institutionId);
            log.info("InstitutionId {} found for taxCode: {}", ecAdesione.getInstitutionId(), ecAdesione.getTaxCode());

        } catch (ResourceNotFoundException e) {
            log.warn("Institution not found with taxCode: {}", ecAdesione.getTaxCode());
            ecAdesione.setHttpStatus(404);
            ecAdesione.setHttpMessage("TaxCode not found in database");
            ecAdesione.setWorkStatus(WorkStatus.INSTITUTION_NOT_FOUND);
        } catch (TooManyInstitutionsException e) {
            log.warn(e.getMessage());
            ecAdesione.setHttpStatus(400);
            ecAdesione.setHttpMessage("Too many institutions found for taxCode");
            ecAdesione.setWorkStatus(WorkStatus.TOO_MANY_INSTITUTIONS);
        } catch (Exception e) {
            log.warn("Error while getting institutionId for taxCode: " + ecAdesione.getTaxCode(), e);
            ecAdesione.setHttpStatus(500);
            ecAdesione.setHttpMessage("Error while getting institutionId");
            ecAdesione.setWorkStatus(WorkStatus.ERROR);
        } finally {
            ecAdesioneConnector.save(ecAdesione);
        }
    }

    private void updateJoiningDate(EcAdesione ecAdesione) {
        try {
            log.info("Updating joining date for institutionId: {}", ecAdesione.getInstitutionId());
            internalApiConnector.updateCreatedAt(ecAdesione.getInstitutionId(), ecAdesione.getDateOfJoining());
            ecAdesione.setWorkStatus(WorkStatus.DONE);
            ecAdesione.setHttpStatus(200);
        } catch (FeignException e) {
            ecAdesione.setHttpStatus(e.status());
            ecAdesione.setHttpMessage(e.getMessage());
            ecAdesione.setWorkStatus(WorkStatus.ERROR);
            log.error("Error while updating joining date for tax code: " + ecAdesione.getTaxCode(), e);
        } catch (Exception e) {
            ecAdesione.setHttpStatus(500);
            ecAdesione.setHttpMessage(e.getMessage());
            ecAdesione.setWorkStatus(WorkStatus.ERROR);
            log.error("Error while updating joining date for tax code: " + ecAdesione.getTaxCode(), e);
        } finally {
            ecAdesioneConnector.save(ecAdesione);
        }
    }
}
