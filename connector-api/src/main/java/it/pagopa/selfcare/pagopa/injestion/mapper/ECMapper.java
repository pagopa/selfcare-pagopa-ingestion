package it.pagopa.selfcare.pagopa.injestion.mapper;

import it.pagopa.selfcare.pagopa.injestion.model.csv.ECModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.EC;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public class ECMapper {

    public static EC convertModelToDto(ECModel ecModel) {
        EC ec = null;
        if(ecModel!= null){
            ec = new EC();
            ec.setId(ecModel.getTaxCode());
            ec.setRegisteredOffice(ecModel.getRegisteredOffice());
            ec.setVatNumber(ecModel.getVatNumber());
            ec.setZipCode(ecModel.getZipCode());
            ec.setTaxCode(ecModel.getTaxCode());
            ec.setRecipientCode(ecModel.getRecipientCode());
            ec.setDigitalAddress(ecModel.getDigitalAddress());
            ec.setBusinessName(ecModel.getBusinessName());
        }
        return ec;
    }
}
