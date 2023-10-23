package it.pagopa.selfcare.pagopa.injestion.core;

import java.util.List;

public interface CsvService {

    <T> List<T> readItemsFromCsv(Class<T> csvClass, byte[] file);

    <T> List<T> readItemsFromCsv(Class<T> csvClass, String filePath);

}
