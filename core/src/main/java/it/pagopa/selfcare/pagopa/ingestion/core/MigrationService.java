package it.pagopa.selfcare.pagopa.ingestion.core;


import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface MigrationService {

    <T, D> void migrateEntities(Class<T> modelClass, String filePath, Consumer<D> saveFunction, Function<T, D> convertFunction);

    <T, D> void migrateEntitiesWithBatchId(Class<T> modelClass, String filePath, Consumer<D> saveFunction, BiFunction<T, String, D> convertFunction, String batchId);

}
