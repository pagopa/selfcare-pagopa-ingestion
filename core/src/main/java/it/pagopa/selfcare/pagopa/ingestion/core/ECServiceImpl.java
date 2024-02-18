package it.pagopa.selfcare.pagopa.ingestion.core;

import feign.FeignException;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.ECConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.ingestion.core.util.ParallelUtil;
import it.pagopa.selfcare.pagopa.ingestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.ingestion.mapper.ECMapper;
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
    private final UserConnector userConnector;
    private final InternalApiConnector internalApiConnector;
    private final String csvPath;
    private final int pageSize;

    public ECServiceImpl(
            MigrationService migrationService,
            ECConnector ecConnector,
            UserConnector userConnector,
            InternalApiConnector internalApiConnector,
            @Value("${app.pageSize}") int pageSize,
            @Value("${app.local.ec}") String csvPath) {
        this.migrationService = migrationService;
        this.ecConnector = ecConnector;
        this.userConnector = userConnector;
        this.internalApiConnector = internalApiConnector;
        this.pageSize = pageSize;
        this.csvPath = csvPath;
    }

    @Override
    @Async
    public void persistEC(String batchId) {
        if(StringUtils.hasText(batchId)) {
            migrationService.migrateEntitiesWithBatchId(ECModel.class, csvPath, ecConnector::save, ECMapper::convertModelToDtoWithBatchId, batchId);
        } else {
        migrationService.migrateEntities(ECModel.class, csvPath, ecConnector::save, ECMapper::convertModelToDto);
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
}
