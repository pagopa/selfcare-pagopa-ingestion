package it.pagopa.selfcare.pagopa.injestion.core;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import it.pagopa.selfcare.pagopa.injestion.exception.SelfCarePagoPaInjectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
@Slf4j
public class CsvServiceImpl implements CsvService {

    @Override
    public <T> List<T> readItemsFromCsv(Class<T> csvClass, byte[] csvData) {
        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csvData)))) {

            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(csvClass)
                    .withSeparator(',')
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (Exception e) {
            log.error("Error during CSV reading: {}", e.getMessage());
            throw new SelfCarePagoPaInjectionException("Error during CSV reading: " + e.getMessage(), 400);
        }
    }

    @Override
    public <T> List<T> readItemsFromCsv(Class<T> csvClass, String filePath) {
        try (FileReader fileReader = new FileReader(filePath+".csv");
             CSVReader csvReader = new CSVReader(fileReader)) {

            csvReader.readNext();

            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader)
                    .withType(csvClass)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (Exception e) {
            log.error("Error during CSV reading: {}", e.getMessage());
            throw new SelfCarePagoPaInjectionException("Error during CSV reading: " + e.getMessage(), 400);
        }
    }

    @Override
    public <T> void writeItemsOnCsv(List<T> items, String nameFile, String directoryPath) {
        try (FileWriter writer = new FileWriter(new File(directoryPath, nameFile))) {
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withQuotechar('"')
                    .withSeparator(';')
                    .build();

            beanToCsv.write(items);
            writer.flush();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error("Error during CSV writing: nameFile: {} - directoryPath: {} - itemsSize: {}", nameFile, directoryPath, items.size());
            throw new SelfCarePagoPaInjectionException("Error: " + e.getMessage(), 400);
        }
    }
}
