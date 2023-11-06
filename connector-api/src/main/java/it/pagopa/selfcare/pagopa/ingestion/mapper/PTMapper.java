package it.pagopa.selfcare.pagopa.ingestion.mapper;

import it.pagopa.selfcare.pagopa.ingestion.model.csv.PTModel;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.PT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public class PTMapper {

    public static PT convertModelToDto(PTModel ptModel) {
        PT pt = null;
        if (ptModel!= null) {
            pt = new PT();
            pt.setId(ptModel.getTaxCode());
            pt.setRegisteredOffice(ptModel.getRegisteredOffice());
            pt.setVatNumber(ptModel.getVatNumber());
            pt.setBusinessName(ptModel.getBusinessName());
            pt.setTaxCode(ptModel.getTaxCode());
            pt.setZipCode(ptModel.getZipCode());
            pt.setDigitalAddress(ptModel.getDigitalAddress());
            pt.setRegisteredOffice(ptModel.getRegisteredOffice());
        }
        return pt;
    }
}