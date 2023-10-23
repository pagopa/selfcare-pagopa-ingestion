package it.pagopa.selfcare.pagopa.injestion.core;

import com.opencsv.bean.CsvToBeanBuilder;
import it.pagopa.selfcare.pagopa.injestion.exception.SelfCarePagoPaInjectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class CsvServiceImpl implements CsvService {

    @Override
    public <T> List<T> readItemsFromCsv(Class<T> csvClass, byte[] csvData) {
        try {
            StringReader stringReader = new StringReader(new String(csvData, StandardCharsets.UTF_8));
            CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>(stringReader);
            csvToBeanBuilder.withSeparator(';');
            csvToBeanBuilder.withSkipLines(1);
            csvToBeanBuilder.withType(csvClass);

            List<T> parsedItems = csvToBeanBuilder.build().parse();
            return new ArrayList<>(parsedItems);
        } catch (Exception e) {
            log.error("Error during CSV reading: {}", e.getMessage());
            throw new SelfCarePagoPaInjectionException("Error during CSV reading: " + e.getMessage(), 400);
        }
    }

    @Override
    public <T> List<T> readItemsFromCsv(Class<T> csvClass, String filePath) {
        try(FileInputStream fileInputStream = new FileInputStream(filePath)){

            StringReader stringReader = new StringReader(new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8));
            CsvToBeanBuilder<T> csvToBeanBuilder = new CsvToBeanBuilder<>(stringReader);
            csvToBeanBuilder.withSeparator(';');
            csvToBeanBuilder.withSkipLines(1);
            csvToBeanBuilder.withType(csvClass);

            List<T> parsedItems = csvToBeanBuilder.build().parse();
            return new ArrayList<>(parsedItems);
        } catch (Exception e) {
            log.error("Error during CSV reading: {}", e.getMessage());
            throw new SelfCarePagoPaInjectionException("Error during CSV reading: " + e.getMessage(), 400);
        }
    }
}
