package it.pagopa.selfcare.pagopa.injestion.core;

import java.util.List;

public interface CsvService {

    <T> List<T> readItemsFromCsv(Class<T> csvClass, byte[] file, int skipLines);

    <T> List<T> readItemsFromCsv(Class<T> csvClass, String filePath, int skipLines);

    <T> void writeItemsOnCsv(List<T> items, String nameFile, String directoryPath);

}
