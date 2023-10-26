package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.pagopa.injestion.api.mongo.PTConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.ExternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.injestion.mapper.PTMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.PTModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import it.pagopa.selfcare.pagopa.injestion.utils.MaskData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.pagopa.injestion.core.util.MigrationUtil.constructOnboardingDto;


@Service
@Slf4j
class PTServiceImpl implements PTService {

    private final MigrationService migrationService;
    private final PTConnector ptConnector;
    private final UserConnector userConnector;
    private final ExternalApiConnector externalApiConnector;
    private final String csvPath;
    private final int pageSize;

    public PTServiceImpl(
            MigrationService migrationService,
            PTConnector ptConnector,
            UserConnector userConnector,
            ExternalApiConnector externalApiConnector,
            @Value("${app.pageSize}") int pageSize,
            @Value("${app.local.pt}") String csvPath) {
        this.migrationService = migrationService;
        this.ptConnector = ptConnector;
        this.userConnector = userConnector;
        this.externalApiConnector = externalApiConnector;
        this.pageSize = pageSize;
        this.csvPath = csvPath;
    }

    @Override
    public void persistPT() {
        migrationService.migrateEntities(PTModel.class, csvPath, ptConnector::save, PTMapper::convertModelToDto);

    }


    @Override
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
        List<User> users = userConnector.findAllByInstitutionTaxCode(pt.getTaxCode());
        AutoApprovalOnboarding onboarding = constructOnboardingDto(pt, users);
        processMigratePT(pt, onboarding, users);
    }

    private void processMigratePT(PT pt, AutoApprovalOnboarding onboarding, List<User> users) {
        List<User> userToSave = new ArrayList<>();
        try {
            externalApiConnector.autoApprovalOnboarding("PT", onboarding);
            pt.setWorkStatus(WorkStatus.DONE);
            users.forEach(user -> user.setWorkStatus(WorkStatus.DONE));
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase(WorkStatus.NOT_FOUND_IN_REGISTRY.getValue())) {
                log.error("Error while migrating EC: TaxCode {} not found in registry", MaskData.maskData(pt.getTaxCode()), e);
                pt.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY);
                userToSave.addAll(users.stream().peek(user -> user.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY)).collect(Collectors.toList()));
            }
            log.error("Error while migrating EC for tax code: " + MaskData.maskData(pt.getTaxCode()), e);
            pt.setWorkStatus(WorkStatus.ERROR);
            userToSave.addAll(users.stream().peek(user -> user.setWorkStatus(WorkStatus.ERROR)).collect(Collectors.toList()));
        } finally {
            ptConnector.save(pt);
            userConnector.saveAll(userToSave);
        }
    }
}
