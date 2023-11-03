package it.pagopa.selfcare.pagopa.ingestion.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Slf4j
class MigrationServiceImpl implements MigrationService {

    private final CsvService csvService;

    public MigrationServiceImpl(CsvService csvService) {
        this.csvService = csvService;
    }

    @Override
    public <T, D> void migrateEntities(Class<T> modelClass, String filePath, Consumer<D> saveFunction, Function<T, D> convertFunction) {
        log.info("Starting migration of " + modelClass.getSimpleName());
        List<T> models = getEntities(modelClass, filePath);
        saveEntitiesParallel(models, saveFunction, convertFunction);
        log.info(String.format("Completed migration of %s %s", models.size(), modelClass.getSimpleName()));
    }

    private <T> List<T> getEntities(Class<T> modelClass, String file) {
        return csvService.readItemsFromCsv(modelClass, file);
    }

    private <T, D> void saveEntitiesParallel(List<T> models, Consumer<D> saveFunction, Function<T, D> convertFunction) {
        CompletableFuture<Void> allOf = CompletableFuture.allOf(models.stream()
                .map(model -> CompletableFuture.runAsync(() -> saveFunction.accept(convertFunction.apply(model)))).toArray(CompletableFuture[]::new));

        allOf.join();
    }
}
