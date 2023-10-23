package it.pagopa.selfcare.pagopa.injestion.core;

import it.pagopa.selfcare.commons.base.utils.Origin;
import it.pagopa.selfcare.pagopa.injestion.api.azure.AzureConnector;
import it.pagopa.selfcare.pagopa.injestion.constant.WorkStatus;
import it.pagopa.selfcare.pagopa.injestion.model.dto.LegalAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Slf4j
class MigrationServiceImpl implements MigrationService {

    private final CsvService csvService;
    private final AzureConnector azureConnector;
    private final boolean local;

    public MigrationServiceImpl(
            CsvService csvService,
            AzureConnector azureConnector,
            @Value("${app.local.csv}") boolean local
    ) {
        this.csvService = csvService;
        this.azureConnector = azureConnector;
        this.local = local;
    }

    @Override
    public <T, D> void migrateEntities(Class<T> modelClass, String filePath, Consumer<D> saveFunction, Function<T, D> convertFunction) {
        log.info("Starting migration of " + modelClass.getSimpleName() + "s");
        List<T> models = getEntities(modelClass, filePath);
        saveEntitiesParallel(models, saveFunction, convertFunction);
        log.info("Completed migration of " + modelClass.getSimpleName() + "s");
    }

    private <T> List<T> getEntities(Class<T> modelClass, String file) {
        if(local){
            return csvService.readItemsFromCsv(modelClass, file);
        }
        else{
            return csvService.readItemsFromCsv(modelClass, azureConnector.readCsv(file).getData());
        }
    }

    private <T, D> void saveEntitiesParallel(List<T> models, Consumer<D> saveFunction, Function<T, D> convertFunction) {
        CompletableFuture<Void> allOf = CompletableFuture.allOf(models.stream()
                .map(model -> CompletableFuture.runAsync(() -> saveFunction.accept(convertFunction.apply(model)))).toArray(CompletableFuture[]::new));

        allOf.join();
    }
}
