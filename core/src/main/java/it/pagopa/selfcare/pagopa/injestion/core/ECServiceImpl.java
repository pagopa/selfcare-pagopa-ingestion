package it.pagopa.selfcare.pagopa.injestion.core;

import feign.FeignException;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.ECConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.ExternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.injestion.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.ECModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import it.pagopa.selfcare.pagopa.injestion.utils.MaskData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.pagopa.injestion.core.util.MigrationUtil.*;

@Service
@Slf4j
class ECServiceImpl implements ECService {

    private final MigrationService migrationService;
    private final ECConnector ecConnector;
    private final UserConnector userConnector;
    private final ExternalApiConnector externalApiConnector;
    private final String csvPath;
    private final int pageSize;

    public ECServiceImpl(
            MigrationService migrationService,
            ECConnector ecConnector,
            UserConnector userConnector,
            ExternalApiConnector externalApiConnector,
            @Value("${app.pageSize}") int pageSize,
            @Value("${app.local.ec}") String csvPath) {
        this.migrationService = migrationService;
        this.ecConnector = ecConnector;
        this.userConnector = userConnector;
        this.externalApiConnector = externalApiConnector;
        this.pageSize = pageSize;
        this.csvPath = csvPath;
    }

    @Override
    public void persistEC() {
        migrationService.migrateEntities(ECModel.class, csvPath, ecConnector::save, ECMapper::convertModelToDto);
    }

    @Override
    public void migrateEC(String status) {
        log.info("Starting migration of EC");
        int page = 0;
        boolean hasNext = true;
        do {
            List<EC> ecList = ecConnector.findAllByStatus(page, pageSize, status);
            if (!CollectionUtils.isEmpty(ecList)) {
                ecList.forEach(this::onboardEc);
            } else {
                hasNext = false;
            }
        } while (Boolean.TRUE.equals(hasNext));

        log.info("Completed migration of PT");
    }

    private void onboardEc(EC ec) {
        List<User> users = userConnector.findAllByInstitutionTaxCode(ec.getTaxCode());
        AutoApprovalOnboarding onboarding = constructOnboardingDto(ec, users);
        processMigrateEC(ec, onboarding, users);
    }

    private void processMigrateEC(EC ec, AutoApprovalOnboarding onboarding, List<User> users) {
        List<User> userToSave = new ArrayList<>();
        try {
            externalApiConnector.autoApprovalOnboarding("EC", onboarding);
            ec.setWorkStatus(WorkStatus.DONE);
            users.forEach(user -> user.setWorkStatus(WorkStatus.DONE));
        } catch (FeignException e) {
            if (e.getMessage().equalsIgnoreCase(WorkStatus.NOT_FOUND_IN_REGISTRY.getValue())){
                log.error("Error while migrating EC: TaxCode {} not found in registry", MaskData.maskData(ec.getTaxCode()), e);
                ec.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY);
                userToSave.addAll(users.stream().peek(user -> user.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY)).collect(Collectors.toList()));
            }
            log.error("Error while migrating EC for tax code: " + MaskData.maskData(ec.getTaxCode()), e);
            ec.setWorkStatus(WorkStatus.ERROR);
            ec.setOnboardinHttpStatus(e.status());
            userToSave.addAll(users.stream().peek(user -> user.setWorkStatus(WorkStatus.ERROR)).collect(Collectors.toList()));
        } finally {
            ecConnector.save(ec);
            userConnector.saveAll(userToSave);
        }
    }
}
