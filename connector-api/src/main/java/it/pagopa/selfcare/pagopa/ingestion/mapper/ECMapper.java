package it.pagopa.selfcare.pagopa.ingestion.mapper;

import it.pagopa.selfcare.pagopa.ingestion.model.csv.ECAdesioneModel;
import it.pagopa.selfcare.pagopa.ingestion.model.csv.ECModel;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.EC;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.EcAdesione;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import static it.pagopa.selfcare.pagopa.ingestion.utils.Utils.convertStringToDate;

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

    public static EC convertModelToDtoWithBatchId(ECModel ecModel, String batchId) {
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
            ec.setBatchId(batchId);
        }
        return ec;
    }

    public static EcAdesione convertModelToDto(ECAdesioneModel ecAdesioneModel) {
        return convertModelToDtoWithBatchId(ecAdesioneModel, null);
    }

    public static EcAdesione convertModelToDtoWithBatchId(ECAdesioneModel ecAdesioneModel, String batchId) {
        EcAdesione ecAdesione = null;
        if(ecAdesioneModel!= null){
            ecAdesione = new EcAdesione();
            ecAdesione.setId(ecAdesioneModel.getTaxCode());
            ecAdesione.setTaxCode(ecAdesioneModel.getTaxCode());
            ecAdesione.setBusinessName(ecAdesioneModel.getBusinessName());
            ecAdesione.setDateOfJoining(ecAdesioneModel.getDateOfJoining());
            ecAdesione.setBatchId(batchId);
        }
        return ecAdesione;
    }
}
