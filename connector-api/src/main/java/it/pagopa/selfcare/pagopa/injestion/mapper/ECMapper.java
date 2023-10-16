package it.pagopa.selfcare.pagopa.injestion.mapper;

import it.pagopa.selfcare.pagopa.injestion.model.csv.ECModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.EC;

import java.util.List;

public class ECMapper {

    public static EC convertModelToDto(ECModel ecModel) {
        EC ec = null;
        if(ecModel!= null){
            ec = new EC();
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

    public static ECModel convertDtoToModel(EC ec) {
        ECModel ecModel = null;
        if(ec!= null){
            ecModel = new ECModel();
            ecModel.setRegisteredOffice(ec.getRegisteredOffice());
            ecModel.setVatNumber(ec.getVatNumber());
            ecModel.setZipCode(ec.getZipCode());
            ecModel.setTaxCode(ec.getTaxCode());
            ecModel.setRecipientCode(ec.getRecipientCode());
            ecModel.setDigitalAddress(ec.getDigitalAddress());
            ecModel.setBusinessName(ec.getBusinessName());
        }
        return ecModel;
    }

    public static List<EC> convertModelsToDto(List<ECModel> ecModelList) {
        List<EC> ecList = null;
        if(ecModelList!= null){
            ecList = ecModelList.stream().map(ECMapper::convertModelToDto).collect(java.util.stream.Collectors.toList());
        }
        return ecList;
    }

    public static List<ECModel> convertDtoToModels(List<EC> ecList) {
        List<ECModel> ecModelList = null;
        if(ecList!= null){
            ecModelList = ecList.stream().map(ECMapper::convertDtoToModel).collect(java.util.stream.Collectors.toList());
        }
        return ecModelList;
    }
}
