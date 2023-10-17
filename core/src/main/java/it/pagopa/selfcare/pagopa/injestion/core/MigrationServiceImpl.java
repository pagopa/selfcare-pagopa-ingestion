package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.pagopa.injestion.api.azure.AzureConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.ECConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.ECIntermediarioPTConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.PTConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.api.rest.SelfcareExternalApiBackendConnector;
import it.pagopa.selfcare.pagopa.injestion.mapper.ECIntermediaroPTMapper;
import it.pagopa.selfcare.pagopa.injestion.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.injestion.mapper.PTMapper;
import it.pagopa.selfcare.pagopa.injestion.mapper.UserMapper;
import it.pagopa.selfcare.pagopa.injestion.model.csv.ECIntermediarioPTModel;
import it.pagopa.selfcare.pagopa.injestion.model.csv.ECModel;
import it.pagopa.selfcare.pagopa.injestion.model.csv.PTModel;
import it.pagopa.selfcare.pagopa.injestion.model.csv.UserModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
class MigrationServiceImpl implements MigrationService {

    private final CsvService csvService;
    private final ECIntermediarioPTConnector ecIntermediarioPTConnector;
    private final ECConnector ecConnector;
    private final PTConnector ptConnector;
    private final UserConnector userConnector;
    private final AzureConnector azureConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final SelfcareExternalApiBackendConnector selfcareExternalApiBackendConnector;
    private final boolean local;
    private final String ecCsvPath;
    private final String ptCsvPath;
    private final String ecPtCsvPath;
    private final String userCsvPath;

    public MigrationServiceImpl(
            CsvService csvService,
            ECIntermediarioPTConnector ecIntermediarioPTConnector,
            ECConnector ecConnector,
            PTConnector ptConnector,
            UserConnector userConnector,
            AzureConnector azureConnector,
            PartyRegistryProxyConnector partyRegistryProxyConnector,
            SelfcareExternalApiBackendConnector selfcareExternalApiBackendConnector,
            @Value("${app.local.csv}") boolean local,
            @Value("${app.local.ec}") String ecCsvPath,
            @Value("${app.local.pt}") String ptCsvPath,
            @Value("${app.local.ecPt}") String ecPtCsvPath,
            @Value("${app.local.user}") String userCsvPath
    ) {
        this.csvService = csvService;
        this.ecIntermediarioPTConnector = ecIntermediarioPTConnector;
        this.ecConnector = ecConnector;
        this.ptConnector = ptConnector;
        this.userConnector = userConnector;
        this.azureConnector = azureConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.selfcareExternalApiBackendConnector = selfcareExternalApiBackendConnector;
        this.local = local;
        this.ecCsvPath = ecCsvPath;
        this.ptCsvPath = ptCsvPath;
        this.ecPtCsvPath = ecPtCsvPath;
        this.userCsvPath = userCsvPath;
    }

    @Override
    public void migrateECIntermediarioPTs() {
        log.info("Starting migration of ECIntermediarioPTs");
        List<ECIntermediarioPTModel> ecIntermediarioPTModels = getECIntermediarioPTs(ecPtCsvPath);
        saveECIntermediarioPTsParallel(ecIntermediarioPTModels);
        log.info("Completed migration of ECIntermediarioPTs");
    }

    private List<ECIntermediarioPTModel> getECIntermediarioPTs(String file) {
        return local ? csvService.readItemsFromCsv(ECIntermediarioPTModel.class, file.getBytes(), 1) : csvService.readItemsFromCsv(ECIntermediarioPTModel.class, azureConnector.readCsv(file).getData(), 1);
    }

    private void saveECIntermediarioPTsParallel(List<ECIntermediarioPTModel> ecIntermediarioPTModels) {
        CompletableFuture<Void> allOf = CompletableFuture.allOf(ecIntermediarioPTModels.stream()
                .map(ecIntermediarioPTModel -> CompletableFuture.runAsync(() -> saveECIntermediarioPT(ecIntermediarioPTModel))).toArray(CompletableFuture[]::new));

        allOf.join();
    }

    private void saveECIntermediarioPT(ECIntermediarioPTModel ecIntermediarioPTModel) {
        ecIntermediarioPTConnector.save(ECIntermediaroPTMapper.convertModelToDto(ecIntermediarioPTModel));
    }

    @Override
    public void migrateECs() {
        log.info("Starting migration of ECs");
        List<ECModel> ecModels = getECs(ecCsvPath);
        saveECsParallel(ecModels);
        log.info("Completed migration of ECs");
    }

    private List<ECModel> getECs(String file) {
        return local ? csvService.readItemsFromCsv(ECModel.class, file.getBytes(), 1) : csvService.readItemsFromCsv(ECModel.class, azureConnector.readCsv(file).getData(), 1);
    }

    private void saveECsParallel(List<ECModel> ecModels) {
        CompletableFuture<Void> allOf = CompletableFuture.allOf(ecModels.stream()
                .map(ecModel -> CompletableFuture.runAsync(() -> saveEC(ecModel))).toArray(CompletableFuture[]::new));

        allOf.join();
    }

    private void saveEC(ECModel ecModel) {
        ecConnector.save(ECMapper.convertModelToDto(ecModel));
    }

    @Override
    public void migratePTs() {
        log.info("Starting migration of PTs");
        List<PTModel> ptModels = getPTs(ptCsvPath);
        savePTsParallel(ptModels);
        log.info("Completed migration of PTs");
    }

    private List<PTModel> getPTs(String file) {
        return local ? csvService.readItemsFromCsv(PTModel.class, file.getBytes(), 1) : csvService.readItemsFromCsv(PTModel.class, azureConnector.readCsv(file).getData(), 1);
    }

    private void savePTsParallel(List<PTModel> ptModels) {
        CompletableFuture<Void> allOf = CompletableFuture.allOf(ptModels.stream()
                .map(ptModel -> CompletableFuture.runAsync(() -> savePT(ptModel))).toArray(CompletableFuture[]::new));

        allOf.join();
    }

    private void savePT(PTModel ptModel) {
        ptConnector.save(PTMapper.convertModelToDto(ptModel));
    }

    @Override
    public void migrateUsers() {
        log.info("Starting migration of Users");
        List<UserModel> userModels = getUsers(userCsvPath);
        saveUsersParallel(userModels);
        log.info("Completed migration of Users");
    }

    private List<UserModel> getUsers(String file) {
        return local ? csvService.readItemsFromCsv(UserModel.class, file.getBytes(), 1) : csvService.readItemsFromCsv(UserModel.class, azureConnector.readCsv(file).getData(), 1);
    }

    private void saveUsersParallel(List<UserModel> userModels) {
        CompletableFuture<Void> allOf = CompletableFuture.allOf(userModels.stream()
                .map(userModel -> CompletableFuture.runAsync(() -> saveUser(userModel))).toArray(CompletableFuture[]::new));

        allOf.join();
    }

    private void saveUser(UserModel userModel) {
        userConnector.save(UserMapper.convertModelToDto(userModel));
    }

    @Override
    public void migrateEC() {
        log.info("Starting migration of EC");
        int page = 0;
        int pageSize = 50; // Considera la possibilità di utilizzare una variabile d'ambiente

        while (true) {
            List<EC> ecs = ecConnector.findAll(page, pageSize);
            if (ecs.isEmpty()) {
                break;
            }

            for (EC ec : ecs) {
                migrateECOnboarding(ec);
            }

            page++;
        }

        log.info("Completed migration of EC");
    }

    private void migrateECOnboarding(EC ec) {
        String taxId = ec.getTaxCode();
        Institution institution = partyRegistryProxyConnector.findInstitution(taxId, null, Optional.empty());

        if (institution != null) {
            migrateECOnboardingWithInstitution(ec, institution);
        } else {
            ec.setStatus(Status.TO_BUILDING);
            ecConnector.save(ec);

            CompletableFuture.supplyAsync(() -> {
                LegalAddress legalAddress = partyRegistryProxyConnector.getLegalAddress(taxId);
                migrateECOnboardingWithLegalAddress(ec, legalAddress);
                return null;
            });
        }
    }

    private void migrateECOnboardingWithInstitution(EC ec, Institution institution) {
        String taxId = ec.getTaxCode();
        Onboarding onboarding = createOnboarding(taxId, institution.getAddress(), institution.getZipCode(), ec, Origin.IPA.name());
        processMigrateEC(ec, taxId, onboarding);
    }

    private void migrateECOnboardingWithLegalAddress(EC ec, LegalAddress legalAddress) {
        String taxId = ec.getTaxCode();
        Onboarding onboarding = createOnboarding(taxId, legalAddress.getAddress(), legalAddress.getZipCode(), ec, Origin.INFOCAMERE.name());
        processMigrateEC(ec, taxId, onboarding);
    }

    private void processMigrateEC(EC ec, String taxId, Onboarding onboarding) {
        ec.setStatus(Status.TO_SENT);
        ecConnector.save(ec);

        try {
            selfcareExternalApiBackendConnector.autoApprovalOnboarding(taxId, "prod-pagopa", onboarding);
            ec.setStatus(Status.DONE);
        } catch (Exception e) {
            ec.setStatus(Status.ERROR);
            log.error("Error migrating EC for tax code: " + taxId, e);
        } finally {
            ecConnector.save(ec);
        }
    }
    private Onboarding createOnboarding(String taxCode, String address, String zipCode, EC ec, String origin) {
        Onboarding onboarding = new Onboarding();
        onboarding.setUsers(userConnector.findAllByTaxCode(taxCode));
        onboarding.setBillingData(fillBillingDataFromInstitutionAndEC(taxCode, address, zipCode, ec));
        onboarding.setInstitutionType(InstitutionType.PA); // Fisso PA (?)
        onboarding.setOrigin(origin);
        onboarding.setGeographicTaxonomies(new ArrayList<>());
        //onboarding.setCompanyInformation(); ???
        //onboarding.setPricingPlan(); ???
        //onboarding.setPspData(new PspData()); ???
        onboarding.setAssistanceContacts(new AssistanceContacts());
        return onboarding;
    }

    private BillingData fillBillingDataFromInstitutionAndEC(String taxCode, String address, String zipCode, EC ec) {
        BillingData billingData = new BillingData();
        billingData.setBusinessName(ec.getBusinessName());
        billingData.setDigitalAddress(address);
        billingData.setRecipientCode(ec.getRecipientCode());
        billingData.setRegisteredOffice(ec.getRegisteredOffice());
        billingData.setTaxCode(taxCode);
        billingData.setZipCode(zipCode);
        billingData.setVatNumber(ec.getVatNumber());
        //billingData.setPublicServices(); ????
        return billingData;
    }

}
