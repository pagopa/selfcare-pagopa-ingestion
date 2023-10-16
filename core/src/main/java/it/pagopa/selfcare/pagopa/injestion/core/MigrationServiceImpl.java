package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.pagopa.injestion.connector.*;
import it.pagopa.selfcare.pagopa.injestion.core.mapper.ECIntermediaroPTMapper;
import it.pagopa.selfcare.pagopa.injestion.core.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.injestion.core.mapper.PTMapper;
import it.pagopa.selfcare.pagopa.injestion.core.mapper.UserMapper;
import it.pagopa.selfcare.pagopa.injestion.core.model.ECIntermediarioPTModel;
import it.pagopa.selfcare.pagopa.injestion.core.model.ECModel;
import it.pagopa.selfcare.pagopa.injestion.core.model.PTModel;
import it.pagopa.selfcare.pagopa.injestion.core.model.UserModel;
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
    private final AzureConnector azureConnector;
    private final boolean local;
    private final String fileName;

    public MigrationServiceImpl(
            CsvService csvService,
            ECIntermediarioPTConnector ecIntermediarioPTConnector,
            ECConnector ecConnector,
            PTConnector ptConnector,
            UserConnector userConnector,
            AzureConnector azureConnector,
            @Value("${app.local.csv}") boolean local,
            @Value("${app.local.filename}") String fileName) {
        this.csvService = csvService;
        this.ecIntermediarioPTConnector = ecIntermediarioPTConnector;
        this.ecConnector = ecConnector;
        this.ptConnector = ptConnector;
        this.userConnector = userConnector;
        this.azureConnector = azureConnector;
        this.local = local;
        this.fileName = fileName;
    }

    @Override
    public void migrateECIntermediarioPTs() {
        log.info("Starting migration of ECIntermediarioPTs");
        List<ECIntermediarioPTModel> ecIntermediarioPTModels = getECIntermediarioPTs(fileName);
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
        List<ECModel> ecModels = getECs(fileName);
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
        List<PTModel> ptModels = getPTs(fileName);
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
        List<UserModel> userModels = getUsers(fileName);
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

}
