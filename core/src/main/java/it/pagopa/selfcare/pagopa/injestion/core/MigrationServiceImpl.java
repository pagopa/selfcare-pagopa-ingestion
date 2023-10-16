package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.pagopa.injestion.api.azure.AzureConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.*;
import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
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
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
class MigrationServiceImpl implements MigrationService {

    private final CsvService csvService;
    private final ECIntermediarioPTConnector ecIntermediarioPTConnector;
    private final ECConnector ecConnector;
    private final PTConnector ptConnector;
    private final UserConnector userConnector;
    private final OnboardingConnector onboardingConnector;
    private final AzureConnector azureConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
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
            OnboardingConnector onboardingConnector,
            AzureConnector azureConnector,
            PartyRegistryProxyConnector partyRegistryProxyConnector,
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
        this.onboardingConnector = onboardingConnector;
        this.azureConnector = azureConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
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
        boolean toNext = true;
        int page = 0;
        int pageSize = 50; // Var d'ambiente (?)

        while (toNext) {
            List<EC> ecs = ecConnector.findAll(page, pageSize);
            if (ecs.isEmpty()) {
                toNext = false;
            } else {
                for (EC ec : ecs) {
                    migrateECOnboarding(ec);
                }
                page = page + 1;
            }
        }

        log.info("Completed migration of EC");
    }

    private void migrateECOnboarding(EC ec) {
        Onboarding onboarding = new Onboarding();
        String taxId = ec.getTaxCode();
        BillingData billingData = new BillingData();
        billingData.setTaxCode(taxId);
        Institution institution = partyRegistryProxyConnector.findInstitution(taxId, null, java.util.Optional.empty());

        if (institution != null) {
            fillBillingDataFromInstitution(billingData, institution);
            onboarding.setStatus(Status.TO_SENT);
            onboarding.setOrigin(Origin.IPA);
            onboarding.setInstitutionType("PA");
            onboarding.setGeographicTaxonomies(new ArrayList<>());
            onboarding.setAssistanceContracts(new AssistanceContracts());
            onboarding.setUsers(userConnector.findAllByTaxCode(taxId));
        } else {
            onboarding.setStatus(Status.TO_BUILDING);
        }

        onboarding.setBillingData(billingData);
        onboardingConnector.save(onboarding);
    }

    private void fillBillingDataFromInstitution(BillingData billingData, Institution institution) {
        billingData.setDigitalAddress(institution.getAddress());
        billingData.setZipCode(institution.getZipCode());
    }

    public void continueEc() {
        log.info("Continuing migration of EC");
        boolean toNext = true;
        int page = 0;
        int pageSize = 50; // Var d'ambiente (?)

        while (toNext) {
            List<Onboarding> onboardings = onboardingConnector.findAllByStatus(page, pageSize, Status.TO_BUILDING.name());
            if (onboardings.isEmpty()) {
                toNext = false;
            } else {
                for (Onboarding onboarding : onboardings) {
                    continueEcOnboarding(onboarding);
                }
            }
        }

        log.info("Completed continuing migration of EC");
    }

    private void continueEcOnboarding(Onboarding onboarding) {
        String taxId = onboarding.getBillingData().getTaxCode();
        LegalAddress legalAddress = partyRegistryProxyConnector.getLegalAddress(taxId);

        if (legalAddress != null) {
            fillBillingDataFromLegalAddress(onboarding.getBillingData(), legalAddress);
            onboarding.setStatus(Status.DONE);
            onboarding.setOrigin(Origin.INFOCAMERE);
            onboarding.setInstitutionType("PA");
            onboarding.setGeographicTaxonomies(new ArrayList<>());
            onboarding.setAssistanceContracts(new AssistanceContracts());
            onboarding.setUsers(userConnector.findAllByTaxCode(taxId));
            onboardingConnector.save(onboarding);
        }
    }

    private void fillBillingDataFromLegalAddress(BillingData billingData, LegalAddress legalAddress) {
        billingData.setDigitalAddress(legalAddress.getAddress());
        billingData.setZipCode(legalAddress.getZipCode());
    }
}
