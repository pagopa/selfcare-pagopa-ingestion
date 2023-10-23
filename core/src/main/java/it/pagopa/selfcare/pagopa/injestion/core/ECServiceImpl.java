package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.commons.base.utils.Origin;
import it.pagopa.selfcare.pagopa.injestion.api.dao.utils.MaskData;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.ECConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.ExternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.injestion.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.ECModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.commons.base.utils.ProductId.PROD_PAGOPA;
import static it.pagopa.selfcare.pagopa.injestion.core.util.MigrationUtil.*;

@Service
@Slf4j
class ECServiceImpl implements ECService {

    private final MigrationService migrationService;
    private final ECConnector ecConnector;
    private final UserConnector userConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final ExternalApiConnector externalApiConnector;
    private final String csvPath;
    private final int pageSize;

    public ECServiceImpl(
            MigrationService migrationService,
            ECConnector ecConnector,
            UserConnector userConnector,
            PartyRegistryProxyConnector partyRegistryProxyConnector,
            ExternalApiConnector externalApiConnector,
            @Value("${app.pageSize}") int pageSize,
            @Value("${app.local.pt}") String csvPath) {
        this.migrationService = migrationService;
        this.ecConnector = ecConnector;
        this.userConnector = userConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
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
        if (WorkStatus.NOT_WORKED == ec.getWorkStatus()) {
            InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(ec.getTaxCode());
            if (institutionProxyInfo != null) {
                persistEcDataFromIpa(ec, institutionProxyInfo);
            } else {
                retrieveAndPersistDataFromInfocamere(ec);
            }
        }
        List<User> users = userConnector.findAllByInstitutionTaxCode(ec.getTaxCode());
        AutoApprovalOnboarding onboarding = constructOnboardingDto(ec, users);
        processMigrateEC(ec, onboarding, users);
    }

    private void processMigrateEC(EC ec, AutoApprovalOnboarding onboarding, List<User> users) {
        List<User> userToSave = new ArrayList<>();
        try {
            externalApiConnector.autoApprovalOnboarding(ec.getTaxCode(), PROD_PAGOPA.getValue(), onboarding);
            ec.setWorkStatus(WorkStatus.DONE);
            users.forEach(user -> user.setWorkStatus(WorkStatus.DONE));
        } catch (Exception e) {
            log.error("Error while migrating EC for tax code: " + MaskData.maskData(ec.getTaxCode()), e);
            ec.setWorkStatus(WorkStatus.ERROR);
            userToSave.addAll(users.stream().peek(user -> user.setWorkStatus(WorkStatus.ERROR)).collect(Collectors.toList()));
        } finally {
            ecConnector.save(ec);
            userConnector.saveAll(userToSave);
        }
    }

    private void persistEcDataFromIpa(EC ec, InstitutionProxyInfo institution) {
        ec.setRegisteredOffice(institution.getAddress());
        ec.setDigitalAddress(institution.getDigitalAddress());
        ec.setZipCode(institution.getZipCode());
        ec.setOrigin(Origin.IPA);
        ec.setWorkStatus(WorkStatus.TO_SEND);
        ecConnector.save(ec);
    }

    private void retrieveAndPersistDataFromInfocamere(EC ec) {
        LegalAddress legalAddress = partyRegistryProxyConnector.getLegalAddress(ec.getTaxCode());
        if (legalAddress != null) {
            ec.setRegisteredOffice(legalAddress.getAddress());
            ec.setZipCode(legalAddress.getZip());
            ec.setOrigin(Origin.INFOCAMERE);
            ec.setWorkStatus(WorkStatus.TO_SEND);
        } else {
            ec.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY);
        }
        ecConnector.save(ec);
    }
}
