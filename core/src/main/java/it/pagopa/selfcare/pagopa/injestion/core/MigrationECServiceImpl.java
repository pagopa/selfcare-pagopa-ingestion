package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.pagopa.injestion.api.dao.utils.MaskData;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.ECConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.injestion.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.ECModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static it.pagopa.selfcare.pagopa.injestion.core.util.MigrationUtil.*;

@Service
@Slf4j
class MigrationECServiceImpl implements MigrationECService {

    private final MigrationService migrationService;
    private final ECConnector ecConnector;
    private final UserConnector userConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final InternalApiConnector internalApiConnector;

    @Value("${app.local.ec}")
    private String csvPath;

    @Value("${app.pageSize}")
    private int pageSize;

    public MigrationECServiceImpl(
            MigrationService migrationService,
            ECConnector ecConnector,
            UserConnector userConnector,
            PartyRegistryProxyConnector partyRegistryProxyConnector,
            InternalApiConnector internalApiConnector
    ) {
        this.migrationService = migrationService;
        this.ecConnector = ecConnector;
        this.userConnector = userConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.internalApiConnector = internalApiConnector;
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
                ecList.forEach(this::migrateECOnboarding);
            } else {
                hasNext = false;
            }
        } while (Boolean.TRUE.equals(hasNext));

        log.info("Completed migration of PT");
    }

    private void migrateECOnboarding(EC ec) {
        if (WorkStatus.NOT_WORKED == ec.getWorkStatus()) {
            InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(ec.getTaxCode());
            if (isInstitutionValid(institutionProxyInfo)) {
                migrateECOnboardingWithIpa(ec, institutionProxyInfo);
            } else {
                migrateECOnboardingWithSedeLegale(ec);
            }
        } else if (WorkStatus.TO_SEND_INFOCAMERE == ec.getWorkStatus() || WorkStatus.TO_SEND_IPA == ec.getWorkStatus()) {
            continueMigrateECOnboarding(ec);
        }
    }


    private void migrateECOnboardingWithIpa(EC ec, InstitutionProxyInfo institution) {
        ec.setRegisteredOffice(institution.getAddress());
        ec.setDigitalAddress(institution.getDigitalAddress());
        ec.setZipCode(institution.getZipCode());
        ec.setWorkStatus(WorkStatus.TO_SEND_IPA);
        ecConnector.save(ec);
        AutoApprovalOnboarding onboarding = createOnboarding(ec, Origin.IPA.name());
        processMigrateEC(ec, onboarding);
    }

    private void migrateECOnboardingWithSedeLegale(EC ec) {
        LegalAddress legalAddress = partyRegistryProxyConnector.getLegalAddress(ec.getTaxCode());

        if (isLegalAddressValid(legalAddress)) {
            ec.setRegisteredOffice(legalAddress.getAddress());
            ec.setZipCode(legalAddress.getZip());
            ec.setWorkStatus(WorkStatus.TO_SEND_INFOCAMERE);
            ecConnector.save(ec);
            AutoApprovalOnboarding onboarding = createOnboarding(ec, Origin.INFOCAMERE.name());
            processMigrateEC(ec, onboarding);
        } else {
            ec.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY);
            ecConnector.save(ec);
        }
    }

    private void processMigrateEC(EC ec, AutoApprovalOnboarding onboarding) {
        try {
            internalApiConnector.autoApprovalOnboarding(ec.getTaxCode(), "prod-pagopa", onboarding);
            ec.setWorkStatus(WorkStatus.DONE);
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException(ec, e);
        } catch (Exception e) {
            log.error("Error while migrating EC for tax code: " + MaskData.maskData(ec.getTaxCode()), e);
        } finally {
            ecConnector.save(ec);
        }
    }

    private void handleHttpClientErrorException(EC ec, HttpClientErrorException e) {
        if (e.getStatusCode() == HttpStatus.CONFLICT || e.getStatusCode() == HttpStatus.BAD_REQUEST) {
            ec.setWorkStatus(WorkStatus.ERROR);
            log.error("Conflict while migrating EC for tax code: " + MaskData.maskData(ec.getTaxCode()), e);
        } else {
            log.error("Error while migrating EC for tax code: " + MaskData.maskData(ec.getTaxCode()), e);
        }
    }

    private AutoApprovalOnboarding createOnboarding(EC ec, String origin) {
        List<User> users = userConnector.findAllByTaxCode(ec.getTaxCode());
        return constructOnboardingDto(ec, origin, users);
    }


    private void continueMigrateECOnboarding(EC ec) {
        AutoApprovalOnboarding onboarding = createOnboarding(ec, ec.getWorkStatus() == WorkStatus.TO_SEND_IPA ? Origin.IPA.name() : Origin.INIPEC.name());
        processMigrateEC(ec, onboarding);
    }
}
