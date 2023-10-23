package it.pagopa.selfcare.pagopa.injestion.mapper;


import it.pagopa.selfcare.pagopa.injestion.model.csv.PTModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.PT;

import java.util.List;

public class PTMapper {

    public static PT convertModelToDto(PTModel ptModel) {
        PT pt = null;
        if (ptModel!= null) {
            pt = new PT();
            pt.setRegisteredOffice(ptModel.getRegisteredOffice());
            pt.setVatNumber(ptModel.getVatNumber());
            pt.setBusinessName(ptModel.getBusinessName());
            pt.setTaxCode(ptModel.getTaxCode());
            pt.setZipCode(ptModel.getZipCode());
            pt.setDigitalAddress(ptModel.getDigitalAddress());
            pt.setRegisteredOffice(ptModel.getRegisteredOffice());
            pt.setRetry(0);
        }
        return pt;
    }

    public static PTModel convertDtoToModel(PT pt) {
        PTModel ptModel = null;
        if (pt!= null) {
            ptModel = new PTModel();
            ptModel.setRegisteredOffice(pt.getRegisteredOffice());
            ptModel.setVatNumber(pt.getVatNumber());
            ptModel.setBusinessName(pt.getBusinessName());
            ptModel.setTaxCode(pt.getTaxCode());
            ptModel.setZipCode(pt.getZipCode());
            ptModel.setDigitalAddress(pt.getDigitalAddress());
            ptModel.setRegisteredOffice(pt.getRegisteredOffice());
        }
        return ptModel;
    }

    public static List<PT> convertModelsToDto(List<PTModel> ptModelList) {
        List<PT> ptList = null;
        if (ptModelList!= null) {
            ptList = ptModelList.stream().map(PTMapper::convertModelToDto).collect(java.util.stream.Collectors.toList());
        }
        return ptList;
    }

    public static List<PTModel> convertDtoToModels(List<PT> ptList) {
        List<PTModel> ptModelList = null;
        if (ptList!= null) {
            ptModelList = ptList.stream().map(PTMapper::convertDtoToModel).collect(java.util.stream.Collectors.toList());
        }
        return ptModelList;
    }
}
