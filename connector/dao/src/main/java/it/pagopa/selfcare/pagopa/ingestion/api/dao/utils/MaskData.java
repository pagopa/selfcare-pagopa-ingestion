package it.pagopa.selfcare.pagopa.ingestion.api.dao.utils;

public class MaskData {


    public static String maskData(String taxId) {
        if (taxId == null || taxId.length() < 5) {
            return taxId;
        }

        StringBuilder censoredCodiceFiscale = new StringBuilder();
        censoredCodiceFiscale.append(taxId, 0, 3);

        for (int i = 3; i < taxId.length() - 1; i++) {
            censoredCodiceFiscale.append('*');
        }

        censoredCodiceFiscale.append(taxId.charAt(taxId.length() - 1));

        return censoredCodiceFiscale.toString();
    }
}
