package it.pagopa.selfcare.pagopa.injestion.core;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import it.pagopa.selfcare.pagopa.injestion.exception.SelfCarePagoPaInjectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CsvService {

    public <T> List<T> readItemsFromCsv(Class<T> csvClass, byte[] file, int skipLines) {
        StringReader stringReader = new StringReader(new String(file, StandardCharsets.UTF_8));
        CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>(stringReader);
        csvToBeanBuilder.withSeparator(';');
        csvToBeanBuilder.withSkipLines(skipLines);
        csvToBeanBuilder.withType(csvClass);

        List<T> parsedItems = csvToBeanBuilder.build().parse();
        return new ArrayList<>(parsedItems);
    }

    public <T> void writeItemsOnCsv(List<T> items, String nameFile, String directoryPath) {
        try (FileWriter writer = new FileWriter(new File(directoryPath, nameFile))) {
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withQuotechar('"')
                    .withSeparator(';')
                    .build();
            beanToCsv.write(items);
            writer.flush();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error("Errore durante la scrittura csv nameFile: {} - directoryPath: {} - itemsSize: {}", nameFile, directoryPath, items.size());
            throw new SelfCarePagoPaInjectionException("ERRORE: " + e.getMessage());
        }
    }
}
