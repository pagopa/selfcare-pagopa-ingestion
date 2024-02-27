package it.pagopa.selfcare.pagopa.ingestion.utils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static LocalDate convertStringToDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String convertToOffsetDateTime(String date) {
        return date + "T00:00:00Z";
    }
}
