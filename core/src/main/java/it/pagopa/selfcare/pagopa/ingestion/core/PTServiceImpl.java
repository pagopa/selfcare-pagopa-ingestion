package it.pagopa.selfcare.pagopa.ingestion.core;

import feign.FeignException;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.PTConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.ingestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.ingestion.mapper.PTMapper;
import it.pagopa.selfcare.pagopa.ingestion.model.csv.PTModel;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static it.pagopa.selfcare.pagopa.ingestion.core.util.MigrationUtil.constructOnboardingDto;


@Service
@Slf4j
class PTServiceImpl implements PTService {

    private final MigrationService migrationService;
    private final PTConnector ptConnector;
    private final UserConnector userConnector;
    private final InternalApiConnector internalApiConnector;
    private final String csvPath;
    private final int pageSize;

    public PTServiceImpl(
            MigrationService migrationService,
            PTConnector ptConnector,
            UserConnector userConnector,
            InternalApiConnector internalApiConnector,
            @Value("${app.pageSize}") int pageSize,
            @Value("${app.local.pt}") String csvPath) {
        this.migrationService = migrationService;
        this.ptConnector = ptConnector;
        this.userConnector = userConnector;
        this.internalApiConnector = internalApiConnector;
        this.pageSize = pageSize;
        this.csvPath = csvPath;
    }

    @Override
    @Async
    public void persistPT() {
        migrationService.migrateEntities(PTModel.class, csvPath, ptConnector::save, PTMapper::convertModelToDto);

    }


    @Override
    @Async
    public void migratePT(String status) {
        log.info("Starting migration of PT");
        int page = 0;
        boolean hasNext = true;
        do {
            List<PT> ptList = ptConnector.findAllByStatus(page, pageSize, status);
            if (!CollectionUtils.isEmpty(ptList)) {
                ptList.forEach(this::onboardPt);
            } else {
                hasNext = false;
            }
        } while (Boolean.TRUE.equals(hasNext));
        log.info("Completed migration of PT");
    }


    private void onboardPt(PT pt) {
        try {
            User user = userConnector.findManagerByInstitutionTaxCodeAndRole(pt.getTaxCode(), Role.RP);
            if(!user.getTaxCode().equalsIgnoreCase("NO_TAXCODE")){
                AutoApprovalOnboarding onboarding = constructOnboardingDto(pt, List.of(user));
                processMigratePT(pt, onboarding, user);
            }else{
                pt.setWorkStatus(WorkStatus.EMPTY_MANAGER_CF);
                ptConnector.save(pt);
            }
        } catch (ResourceNotFoundException e){
            pt.setWorkStatus(WorkStatus.MANAGER_NOT_FOUND);
            ptConnector.save(pt);
        }

    }

    private void processMigratePT(PT pt, AutoApprovalOnboarding onboarding, User user) {
        try {
            internalApiConnector.autoApprovalOnboarding("PT", onboarding);
            pt.setWorkStatus(WorkStatus.DONE);
            pt.setOnboardinHttpStatus(200);
            user.setWorkStatus(WorkStatus.DONE);
        } catch (FeignException e) {
            if (e.status() == 404){
                log.error("Error while migrating PT: TaxCode {} not found in registry", pt.getTaxCode(), e);
                pt.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY);
                user.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY);
            } else if (e.status() == 409) {
                log.error("Error while migrating PT: product already onboarded for TaxCode {}", pt.getTaxCode(), e);
                pt.setWorkStatus(WorkStatus.ALREADY_ONBOARDED);
                pt.setOnboardinHttpStatus(e.status());
                pt.setOnboardingMessage(e.getMessage());
                user.setWorkStatus(WorkStatus.NOT_WORKED);
            } else {
                log.error("Error while migrating PT for tax code: " + pt.getTaxCode(), e);
                pt.setWorkStatus(WorkStatus.ERROR);
                pt.setOnboardinHttpStatus(e.status());
                pt.setOnboardingMessage(e.getMessage());
                user.setWorkStatus(WorkStatus.ERROR);
            }
        } finally {
            ptConnector.save(pt);
            userConnector.save(user);
        }
    }
}
