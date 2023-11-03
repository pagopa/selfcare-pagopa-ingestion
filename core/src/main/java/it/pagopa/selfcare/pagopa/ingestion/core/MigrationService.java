package it.pagopa.selfcare.pagopa.ingestion.core;


import java.util.function.Consumer;
import java.util.function.Function;

public interface MigrationService {

    <T, D> void migrateEntities(Class<T> modelClass, String filePath, Consumer<D> saveFunction, Function<T, D> convertFunction);



}
