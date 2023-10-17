package it.pagopa.selfcare.pagopa.injestion.core;

import io.micrometer.core.instrument.util.StringUtils;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.pagopa.injestion.api.dao.utils.MaskData;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.ECConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.SelfcareExternalApiBackendConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.SelfcareMsCoreConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.SelfcareMsPartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.ECModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
class MigrationECServiceImpl implements MigrationECService {

    private final MigrationService migrationService;
    private final ECConnector ecConnector;
    private final UserConnector userConnector;
    private final SelfcareMsCoreConnector selfcareMsCoreConnector;
    private final SelfcareExternalApiBackendConnector selfcareExternalApiBackendConnector;
    private final SelfcareMsPartyRegistryProxyConnector selfcareMsPartyRegistryProxyConnector;

    @Value("${app.local.ec}")
    private String csvPath;

    @Value("${app.pageSize}")
    private int pageSize;

    public MigrationECServiceImpl(
            MigrationService migrationService,
            ECConnector ecConnector,
            UserConnector userConnector,
            SelfcareMsCoreConnector selfcareMsCoreConnector,
            SelfcareExternalApiBackendConnector selfcareExternalApiBackendConnector,
            SelfcareMsPartyRegistryProxyConnector selfcareMsPartyRegistryProxyConnector
    ) {
        this.migrationService = migrationService;
        this.ecConnector = ecConnector;
        this.userConnector = userConnector;
        this.selfcareMsCoreConnector = selfcareMsCoreConnector;
        this.selfcareExternalApiBackendConnector = selfcareExternalApiBackendConnector;
        this.selfcareMsPartyRegistryProxyConnector = selfcareMsPartyRegistryProxyConnector;
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
        Institution institution = selfcareMsCoreConnector.createInstitutionFromIpa(ec.getTaxCode(), null, null);

        if (isInstitutionValid(institution)) {
            migrateECOnboardingWithIpa(ec, institution);
        } else {
            migrateECOnboardingWithInipec(ec);
        }
    }

    private boolean isInstitutionValid(Institution institution) {
        return institution != null &&
                StringUtils.isNotEmpty(institution.getAddress()) &&
                StringUtils.isNotEmpty(institution.getDigitalAddress()) &&
                StringUtils.isNotEmpty(institution.getZipCode());
    }

    private void migrateECOnboardingWithIpa(EC ec, Institution institution) {
        ec.setDigitalAddress(institution.getDigitalAddress());
        ec.setZipCode(institution.getZipCode());
        ec.setWorkStatus(WorkStatus.TO_SEND);
        ecConnector.save(ec);
        Onboarding onboarding = createOnboarding(ec, Origin.IPA.name());
        processMigrateEC(ec, onboarding);
    }

    private void migrateECOnboardingWithInipec(EC ec) {
        IniPec iniPec = selfcareMsPartyRegistryProxyConnector.getIniPec(ec.getTaxCode());
        ec.setDigitalAddress(iniPec.getAddress());
        ec.setZipCode(iniPec.getZipCode());
        ec.setWorkStatus(WorkStatus.TO_SEND);
        ecConnector.save(ec);
        Onboarding onboarding = createOnboarding(ec, Origin.INFOCAMERE.name());
        processMigrateEC(ec, onboarding);
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
            ec.setWorkStatus(WorkStatus.TO_SEND);
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
        onboarding.setInstitutionType(InstitutionType.PA); // Fisso PA (?)
        onboarding.setOrigin(origin);
        onboarding.setGeographicTaxonomies(new ArrayList<>());
        //onboarding.setCompanyInformation(); ???
        //onboarding.setPricingPlan(); ???
        //onboarding.setPspData(new PspData()); ???
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
        //billingData.setPublicServices(); ????
        return billingData;
    }

    @Override
    public void autoComplete() {
        log.info("Starting auto complete of EC");
        int page = 0;

        while (true) {
            List<EC> ecs = ecConnector.findAllByStatus(page, pageSize, WorkStatus.TO_SEND.name());
            if (ecs.isEmpty()) {
                break;
            }

            ecs.forEach(this::continueMigrateECOnboarding);

            page++;
        }

        log.info("Completed auto complete of EC");
    }

    private void continueMigrateECOnboarding(EC ec){
        Onboarding onboarding = createOnboarding(ec, Origin.INFOCAMERE.name());  //Distinguere ORIGIN
        processMigrateEC(ec, onboarding);
    }



}
