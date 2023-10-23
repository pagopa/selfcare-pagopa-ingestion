package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.pagopa.injestion.api.dao.utils.MaskData;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.PTConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.InternalApiConnector;
import it.pagopa.selfcare.pagopa.injestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.injestion.mapper.PTMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.PTModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
class MigrationPTServiceImpl implements MigrationPTService {

    private final MigrationService migrationService;
    private final PTConnector ptConnector;
    private final UserConnector userConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final InternalApiConnector internalApiConnector;

    @Value("${app.local.ec}")
    private String csvPath;

    @Value("${app.pageSize}")
    private int pageSize;

    public MigrationPTServiceImpl(
            MigrationService migrationService,
            PTConnector ptConnector,
            UserConnector userConnector,
            PartyRegistryProxyConnector partyRegistryProxyConnector,
            InternalApiConnector internalApiConnector
    ) {
        this.migrationService = migrationService;
        this.ptConnector = ptConnector;
        this.userConnector = userConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.internalApiConnector = internalApiConnector;
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
                ptList.forEach(this::migratePTOnboarding);
            } else {
                hasNext = false;
            }
        } while (Boolean.TRUE.equals(hasNext));
        log.info("Completed migration of PT");
    }


    private void migratePTOnboarding(PT pt) {
        InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(pt.getTaxCode());
        if (institutionProxyInfo != null) {
            migratePTOnboardingWithIpa(pt, institutionProxyInfo);
        } else {
            migratePTOnboardingWithSedeLegale(pt);
        }
    }



    private void migratePTOnboardingWithIpa(PT pt, InstitutionProxyInfo institution) {
        pt.setRegisteredOffice(institution.getAddress());
        pt.setDigitalAddress(institution.getDigitalAddress());
        pt.setZipCode(institution.getZipCode());
        pt.setWorkStatus(WorkStatus.TO_SEND_IPA);
        pt.setRetry(pt.getRetry()+1);
        ptConnector.save(pt);
        AutoApprovalOnboarding onboarding = createAutoApprovalOnboarding(pt, Origin.IPA.name());
        processMigratePT(pt, onboarding);
    }

    private void migratePTOnboardingWithSedeLegale(PT pt) {
        LegalAddress legalAddress = partyRegistryProxyConnector.getLegalAddress(pt.getTaxCode());

        if (legalAddress != null) {
            pt.setRegisteredOffice(legalAddress.getAddress());
            pt.setZipCode(legalAddress.getZip());
            pt.setWorkStatus(WorkStatus.TO_SEND_INFOCAMERE);
            pt.setRetry(pt.getRetry()+1);
            ptConnector.save(pt);
            AutoApprovalOnboarding onboarding = createAutoApprovalOnboarding(pt, Origin.INFOCAMERE.name());
            processMigratePT(pt, onboarding);
        } else {
            pt.setWorkStatus(WorkStatus.NOT_FOUND_IN_REGISTRY);
            ptConnector.save(pt);
        }
    }


    private void processMigratePT(PT pt, AutoApprovalOnboarding onboarding) {
        try {
            internalApiConnector.autoApprovalOnboarding(pt.getTaxCode(), "prod-pagopa", onboarding);
            pt.setWorkStatus(WorkStatus.DONE);
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException(pt, e);
        } catch (Exception e) {
            if(pt.getRetry()>2){
                pt.setWorkStatus(WorkStatus.ERROR);
            }
            log.error("Error migrating EC for tax code: " + MaskData.maskData(pt.getTaxCode()), e);
        } finally {
            ptConnector.save(pt);
        }
    }

    private void handleHttpClientErrorException(PT pt, HttpClientErrorException e) {
        if (e.getStatusCode() == HttpStatus.CONFLICT || e.getStatusCode() == HttpStatus.BAD_REQUEST || pt.getRetry()>2) {
            pt.setWorkStatus(WorkStatus.ERROR);
            log.error("Conflict while migrating EC for tax code: " + MaskData.maskData(pt.getTaxCode()), e);
        } else {
            log.error("Error while migrating EC for tax code: " + MaskData.maskData(pt.getTaxCode()), e);
        }
    }

    private AutoApprovalOnboarding createAutoApprovalOnboarding(PT pt, String origin) {
        AutoApprovalOnboarding onboarding = new AutoApprovalOnboarding();
        onboarding.setBillingData(fillBillingDataFromInstitutionAndEC(pt.getDigitalAddress(), pt.getZipCode(), pt));
        onboarding.setInstitutionType(InstitutionType.PA);
        onboarding.setGeographicTaxonomies(List.of());
        onboarding.setOrigin(origin);
        List<UserToOnboard> users = userConnector.findAllByTaxCode(pt.getTaxCode()).stream()
                .map(user -> {
                    UserToOnboard userToSend = new UserToOnboard();
                    userToSend.setTaxCode(user.getTaxCode());
                    userToSend.setName(user.getName());
                    userToSend.setSurname(user.getSurname());
                    userToSend.setEmail(user.getEmail());
                    userToSend.setRole(user.getRole() == Role.RT ? PartyRole.OPERATOR : PartyRole.MANAGER);
                    return userToSend;
                }).collect(Collectors.toList());

        onboarding.setUsers(users);
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
        AutoApprovalOnboarding onboarding =
                createAutoApprovalOnboarding(pt, pt.getWorkStatus() == WorkStatus.TO_SEND_IPA ? Origin.IPA.name() : Origin.INIPEC.name());
        pt.setRetry(pt.getRetry()+1);
        processMigratePT(pt, onboarding);
    }
}
