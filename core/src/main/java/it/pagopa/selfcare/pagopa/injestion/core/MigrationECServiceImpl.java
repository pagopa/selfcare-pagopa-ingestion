package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.pagopa.injestion.api.dao.utils.MaskData;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.ECConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.NationalRegistriesConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.SelfcareExternalApiBackendConnector;
import it.pagopa.selfcare.pagopa.injestion.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.ECModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@Slf4j
class MigrationECServiceImpl implements MigrationECService {

    private final MigrationService migrationService;
    private final ECConnector ecConnector;
    private final UserConnector userConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final SelfcareExternalApiBackendConnector selfcareExternalApiBackendConnector;
    private final NationalRegistriesConnector nationalRegistriesConnector;

    @Value("${app.local.ec}")
    private String csvPath;

    @Value("${app.pageSize}")
    private int pageSize;

    public MigrationECServiceImpl(
            MigrationService migrationService,
            ECConnector ecConnector,
            UserConnector userConnector,
            PartyRegistryProxyConnector partyRegistryProxyConnector,
            SelfcareExternalApiBackendConnector selfcareExternalApiBackendConnector,
            NationalRegistriesConnector nationalRegistriesConnector
    ) {
        this.migrationService = migrationService;
        this.ecConnector = ecConnector;
        this.userConnector = userConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.selfcareExternalApiBackendConnector = selfcareExternalApiBackendConnector;
        this.nationalRegistriesConnector = nationalRegistriesConnector;
    }

    @Override
    public void persistEC() {
        migrationService.migrateEntities(ECModel.class, csvPath, ecConnector::save, ECMapper::convertModelToDto);
    }

    @Override
    public void migrateEC() {
        log.info("Starting migration of EC");

        int page = 0;

        while (true) {
            List<EC> ecs = ecConnector.findAllByStatus(page, pageSize, WorkStatus.NOT_WORKED.name());

            if (ecs.isEmpty()) {
                break;
            }

            ecs.forEach(this::migrateECOnboarding);
            page++;
        }

        log.info("Completed migration of EC");
    }

    private void migrateECOnboarding(EC ec) {
        InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(ec.getTaxCode());

        if (isInstitutionValid(institutionProxyInfo)) {
            migrateECOnboardingWithIpa(ec, institutionProxyInfo);
        } else {
            migrateECOnboardingWithSedeLegale(ec);
        }
    }

    private boolean isInstitutionValid(InstitutionProxyInfo institution) {
        return institution != null &&
                isStringValid(institution.getAddress()) &&
                isStringValid(institution.getDigitalAddress()) &&
                isStringValid(institution.getZipCode());
    }

    private void migrateECOnboardingWithIpa(EC ec, InstitutionProxyInfo institution) {
        ec.setDigitalAddress(institution.getDigitalAddress());
        ec.setZipCode(institution.getZipCode());
        ec.setWorkStatus(WorkStatus.TO_SEND_IPA);
        ecConnector.save(ec);
        Onboarding onboarding = createOnboarding(ec, Origin.IPA.name());
        processMigrateEC(ec, onboarding);
    }

    private void migrateECOnboardingWithSedeLegale(EC ec) {
        LegalAddress legalAddress = nationalRegistriesConnector.getLegalAddress(ec.getTaxCode());

        if (isLegalAddressValid(legalAddress)) {
            ec.setDigitalAddress(legalAddress.getProfessionalAddress().getAddress());
            ec.setZipCode(legalAddress.getProfessionalAddress().getZip());
            ec.setWorkStatus(WorkStatus.TO_SEND_INIPEC);
            ecConnector.save(ec);
            Onboarding onboarding = createOnboarding(ec, Origin.INFOCAMERE.name());
            processMigrateEC(ec, onboarding);
        } else {
            ec.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY);
            ecConnector.save(ec);
        }
    }

    private boolean isLegalAddressValid(LegalAddress legalAddress) {
        return legalAddress != null &&
                isStringValid(legalAddress.getProfessionalAddress().getAddress()) &&
                isStringValid(legalAddress.getProfessionalAddress().getZip());
    }

    private boolean isStringValid(String str) {
        return str != null && !str.isEmpty();
    }

    private void processMigrateEC(EC ec, Onboarding onboarding) {
        try {
            selfcareExternalApiBackendConnector.autoApprovalOnboarding(ec.getTaxCode(), "prod-pagopa", onboarding);
            ec.setWorkStatus(WorkStatus.DONE);
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException(ec, e);
        } catch (Exception e) {
            handleOtherException(ec, e);
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

    private void handleOtherException(EC ec, Exception e) {
        ec.setWorkStatus(WorkStatus.ERROR);
        log.error("Error migrating EC for tax code: " + MaskData.maskData(ec.getTaxCode()), e);
    }

    private Onboarding createOnboarding(EC ec, String origin) {
        Onboarding onboarding = new Onboarding();
        onboarding.setUsers(userConnector.findAllByTaxCode(ec.getTaxCode()));
        onboarding.setBillingData(fillBillingDataFromInstitutionAndEC(ec.getDigitalAddress(), ec.getZipCode(), ec));
        onboarding.setInstitutionType(InstitutionType.PA);
        onboarding.setOrigin(origin);
        onboarding.setGeographicTaxonomies(List.of());
        onboarding.setAssistanceContacts(new AssistanceContacts());
        return onboarding;
    }

    private BillingData fillBillingDataFromInstitutionAndEC(String digitalAddress, String zipCode, EC ec) {
        BillingData billingData = new BillingData();
        billingData.setBusinessName(ec.getBusinessName());
        billingData.setDigitalAddress(digitalAddress);
        billingData.setRecipientCode(ec.getRecipientCode());
        billingData.setRegisteredOffice(ec.getRegisteredOffice());
        billingData.setTaxCode(ec.getTaxCode());
        billingData.setZipCode(zipCode);
        billingData.setVatNumber(ec.getVatNumber());
        return billingData;
    }

    @Override
    public void autoComplete() {
        autoComplete(WorkStatus.TO_SEND_IPA.name());
        autoComplete(WorkStatus.TO_SEND_INIPEC.name());
    }

    private void autoComplete(String workStatus) {
        log.info("Starting auto complete of EC with workStatus: {}", workStatus);
        int page = 0;
        while (true) {
            List<EC> ecs = ecConnector.findAllByStatus(page, pageSize, workStatus);
            if (ecs.isEmpty()) {
                break;
            }

            ecs.forEach(this::continueMigrateECOnboarding);

            page++;
        }

        log.info("Completed auto complete of EC");
    }

    private void continueMigrateECOnboarding(EC ec) {
        Onboarding onboarding = createOnboarding(ec, ec.getWorkStatus() == WorkStatus.TO_SEND_IPA ? Origin.IPA.name() : Origin.INIPEC.name());
        processMigrateEC(ec, onboarding);
    }
}
