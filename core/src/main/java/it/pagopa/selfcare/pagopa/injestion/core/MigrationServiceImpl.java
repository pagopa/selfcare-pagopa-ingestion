package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.pagopa.injestion.api.azure.AzureConnector;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.*;
import it.pagopa.selfcare.pagopa.injestion.api.rest.PartyRegistryProxyConnector;
import it.pagopa.selfcare.pagopa.injestion.core.mapper.ECIntermediaroPTMapper;
import it.pagopa.selfcare.pagopa.injestion.core.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.injestion.core.mapper.PTMapper;
import it.pagopa.selfcare.pagopa.injestion.core.mapper.UserMapper;
import it.pagopa.selfcare.pagopa.injestion.model.*;
import it.pagopa.selfcare.pagopa.injestion.model.csv.ECIntermediarioPTModel;
import it.pagopa.selfcare.pagopa.injestion.model.csv.ECModel;
import it.pagopa.selfcare.pagopa.injestion.model.csv.PTModel;
import it.pagopa.selfcare.pagopa.injestion.model.csv.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public MigrationServiceImpl(
            CsvService csvService,
            ECIntermediarioPTConnector ecIntermediarioPTConnector,
            ECConnector ecConnector,
            PTConnector ptConnector,
            UserConnector userConnector,
            OnboardingConnector onboardingConnector,
            AzureConnector azureConnector,
            PartyRegistryProxyConnector partyRegistryProxyConnector,
            @Value("${app.local.csv}") boolean local) {
        this.csvService = csvService;
        this.ecIntermediarioPTConnector = ecIntermediarioPTConnector;
        this.ecConnector = ecConnector;
        this.ptConnector = ptConnector;
        this.userConnector = userConnector;
        this.onboardingConnector = onboardingConnector;
        this.azureConnector = azureConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.local = local;
    }

    @Override
    public void migrateECIntermediarioPTs(String path) {
        log.info("Starting migration of ECIntermediarioPTs");
        List<ECIntermediarioPTModel> ecIntermediarioPTModels = getECIntermediarioPTs(path);
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
    public void migrateECs(String path) {
        log.info("Starting migration of ECs");
        List<ECModel> ecModels = getECs(path);
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
    public void migratePTs(String path) {
        log.info("Starting migration of PTs");
        List<PTModel> ptModels = getPTs(path);
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
    public void migrateUsers(String path) {
        log.info("Starting migration of Users");
        List<UserModel> userModels = getUsers(path);
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
    public void ec(){
        boolean toNext = true;
        int page = 0;
        int pageSize = 50; //Var d'ambiente (?)

        while(toNext){
            List<EC> ecs = ecConnector.findAll(page, pageSize);
            if(ecs.isEmpty()){
                toNext = false;
            }
            else{
                for (EC ec : ecs) {
                    Onboarding onboarding = new Onboarding();
                    String taxId = ec.getTaxCode();
                    LegalAddress legalAddress = partyRegistryProxyConnector.getLegalAddress(taxId);
                    if(legalAddress!= null){
                        String address = legalAddress.getAddress();
                        String zipCode = legalAddress.getZipCode();
                        BillingData billingData = new BillingData();
                        billingData.setDigitalAddress(address);
                        billingData.setZipCode(zipCode);
                        billingData.setTaxCode(taxId);
                        onboarding.setBillingData(billingData);
                        onboarding.setStatus(Status.TO_SENT);
                        onboarding.setOrigin(Origin.IPA);
                        onboarding.setInstitutionType("PA");
                    }
                    else{
                        onboarding.setStatus(Status.TO_BUILDING);
                    }
                    onboardingConnector.save(onboarding);
                }
                page = page + 1;
            }
        }

    }
}
