package it.pagopa.selfcare.pagopa.ingestion.core;

import java.util.List;

public interface CsvService {

    <T> List<T> readItemsFromCsv(Class<T> csvClass, byte[] file);

    <T> List<T> readItemsFromCsv(Class<T> csvClass, String filePath);

}
