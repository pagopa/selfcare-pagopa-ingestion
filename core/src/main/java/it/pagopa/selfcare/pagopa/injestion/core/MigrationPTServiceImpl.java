package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.pagopa.injestion.api.dao.utils.MaskData;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.PTConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.NationalRegistriesConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.SelfcareExternalApiBackendConnector;
import it.pagopa.selfcare.pagopa.injestion.mapper.PTMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.PTModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@Slf4j
class MigrationPTServiceImpl implements MigrationPTService {

    private final MigrationService migrationService;
    private final PTConnector ptConnector;
    private final UserConnector userConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final SelfcareExternalApiBackendConnector selfcareExternalApiBackendConnector;
    private final NationalRegistriesConnector nationalRegistriesConnector;

    @Value("${app.local.ec}")
    private String csvPath;

    @Value("${app.pageSize}")
    private int pageSize;

    public MigrationPTServiceImpl(
            MigrationService migrationService,
            PTConnector ptConnector,
            UserConnector userConnector,
            PartyRegistryProxyConnector partyRegistryProxyConnector,
            SelfcareExternalApiBackendConnector selfcareExternalApiBackendConnector,
            NationalRegistriesConnector nationalRegistriesConnector
    ) {
        this.migrationService = migrationService;
        this.ptConnector = ptConnector;
        this.userConnector = userConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.selfcareExternalApiBackendConnector = selfcareExternalApiBackendConnector;
        this.nationalRegistriesConnector = nationalRegistriesConnector;
    }

    @Override
    public void persistPT() {
        migrationService.migrateEntities(PTModel.class, csvPath, ptConnector::save, PTMapper::convertModelToDto);
    }


    @Override
    public void migratePT() {
        log.info("Starting migration of PT");

        int page = 0;

        while (true) {
            List<PT> pts = ptConnector.findAllByStatus(page, pageSize, WorkStatus.NOT_WORKED.name());

            if (pts.isEmpty()) {
                break;
            }

            pts.forEach(this::migratePTOnboarding);
            page++;
        }

        log.info("Completed migration of PT");
    }


    private void migratePTOnboarding(PT pt) {
        InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(pt.getTaxCode());

        if (isInstitutionValid(institutionProxyInfo)) {
            migratePTOnboardingWithIpa(pt, institutionProxyInfo);
        } else {
            migratePTOnboardingWithSedeLegale(pt);
        }
    }

    private boolean isInstitutionValid(InstitutionProxyInfo institution) {
        return institution != null &&
                isStringValid(institution.getAddress()) &&
                isStringValid(institution.getDigitalAddress()) &&
                isStringValid(institution.getZipCode());
    }

    private void migratePTOnboardingWithIpa(PT pt, InstitutionProxyInfo institution) {
        pt.setDigitalAddress(institution.getDigitalAddress());
        pt.setZipCode(institution.getZipCode());
        pt.setWorkStatus(WorkStatus.TO_SEND_IPA);
        ptConnector.save(pt);
        Onboarding onboarding = createOnboarding(pt, Origin.IPA.name());
        processMigratePT(pt, onboarding);
    }

    private void migratePTOnboardingWithSedeLegale(PT pt) {
        LegalAddress legalAddress = nationalRegistriesConnector.getLegalAddress(pt.getTaxCode());

        if (isLegalAddressValid(legalAddress)) {
            pt.setDigitalAddress(legalAddress.getProfessionalAddress().getAddress());
            pt.setZipCode(legalAddress.getProfessionalAddress().getZip());
            pt.setWorkStatus(WorkStatus.TO_SEND_INIPEC);
            ptConnector.save(pt);
            Onboarding onboarding = createOnboarding(pt, Origin.INFOCAMERE.name());
            processMigratePT(pt, onboarding);
        } else {
            pt.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY);
            ptConnector.save(pt);
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

    private void processMigratePT(PT pt, Onboarding onboarding) {
        try {
            selfcareExternalApiBackendConnector.autoApprovalOnboarding(pt.getTaxCode(), "prod-pagopa", onboarding);
            pt.setWorkStatus(WorkStatus.DONE);
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException(pt, e);
        } catch (Exception e) {
            handleOtherException(pt, e);
        } finally {
            ptConnector.save(pt);
        }
    }

    private void handleHttpClientErrorException(PT pt, HttpClientErrorException e) {
        if (e.getStatusCode() == HttpStatus.CONFLICT || e.getStatusCode() == HttpStatus.BAD_REQUEST) {
            pt.setWorkStatus(WorkStatus.ERROR);
            log.error("Conflict while migrating EC for tax code: " + MaskData.maskData(pt.getTaxCode()), e);
        } else {
            log.error("Error while migrating EC for tax code: " + MaskData.maskData(pt.getTaxCode()), e);
        }
    }

    private void handleOtherException(PT pt, Exception e) {
        pt.setWorkStatus(WorkStatus.ERROR);
        log.error("Error migrating EC for tax code: " + MaskData.maskData(pt.getTaxCode()), e);
    }

    private Onboarding createOnboarding(PT pt, String origin) {
        Onboarding onboarding = new Onboarding();
        onboarding.setUsers(userConnector.findAllByTaxCode(pt.getTaxCode()));
        onboarding.setBillingData(fillBillingDataFromInstitutionAndEC(pt.getDigitalAddress(), pt.getZipCode(), pt));
        onboarding.setInstitutionType(InstitutionType.PA);
        onboarding.setOrigin(origin);
        onboarding.setGeographicTaxonomies(List.of());
        onboarding.setAssistanceContacts(new AssistanceContacts());
        return onboarding;
    }

    private BillingData fillBillingDataFromInstitutionAndEC(String digitalAddress, String zipCode, PT pt) {
        BillingData billingData = new BillingData();
        billingData.setBusinessName(pt.getBusinessName());
        billingData.setDigitalAddress(digitalAddress);
        billingData.setRegisteredOffice(pt.getRegisteredOffice());
        billingData.setTaxCode(pt.getTaxCode());
        billingData.setZipCode(zipCode);
        billingData.setVatNumber(pt.getVatNumber());
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
            List<PT> pts = ptConnector.findAllByStatus(page, pageSize, workStatus);
            if (pts.isEmpty()) {
                break;
            }

            pts.forEach(this::continueMigratePTOnboarding);

            page++;
        }

        log.info("Completed auto complete of EC");
    }

    private void continueMigratePTOnboarding(PT pt) {
        Onboarding onboarding = createOnboarding(pt, pt.getWorkStatus() == WorkStatus.TO_SEND_IPA ? Origin.IPA.name() : Origin.INIPEC.name());
        processMigratePT(pt, onboarding);
    }
}
