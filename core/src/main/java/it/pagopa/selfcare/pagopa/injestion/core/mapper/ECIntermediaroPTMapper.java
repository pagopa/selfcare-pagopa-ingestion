package it.pagopa.selfcare.pagopa.injestion.core.mapper;


import it.pagopa.selfcare.pagopa.injestion.core.model.ECIntermediarioPTModel;
import it.pagopa.selfcare.pagopa.injestion.dto.ECIntermediarioPT;

import java.util.List;

public class ECIntermediaroPTMapper {

    public static ECIntermediarioPT convertModelToDto(ECIntermediarioPTModel ecIntermediarioPTModel) {
        ECIntermediarioPT ecIntermediaroPT = null;
        if(ecIntermediarioPTModel!= null){
            ecIntermediaroPT = new ECIntermediarioPT();
            ecIntermediaroPT.setEnteIndirettoRagioneSociale(ecIntermediarioPTModel.getEnteIndirettoRagioneSociale());
            ecIntermediaroPT.setIntermediarioPTCF(ecIntermediarioPTModel.getIntermediarioPTCF());
            ecIntermediaroPT.setEnteIndirettoCF(ecIntermediarioPTModel.getEnteIndirettoCF());
            ecIntermediaroPT.setIntermediarioPTRagioneSociale(ecIntermediarioPTModel.getIntermediarioPTRagioneSociale());
        }
        return ecIntermediaroPT;
    }

    public static ECIntermediarioPTModel convertDtoToModel(ECIntermediarioPT ecIntermediarioPT) {
        ECIntermediarioPTModel ecIntermediarioPTModel = null;
        if(ecIntermediarioPT!= null){
            ecIntermediarioPTModel = new ECIntermediarioPTModel();
            ecIntermediarioPTModel.setEnteIndirettoRagioneSociale(ecIntermediarioPT.getEnteIndirettoRagioneSociale());
            ecIntermediarioPTModel.setIntermediarioPTCF(ecIntermediarioPT.getIntermediarioPTCF());
            ecIntermediarioPTModel.setEnteIndirettoCF(ecIntermediarioPT.getEnteIndirettoCF());
            ecIntermediarioPTModel.setIntermediarioPTRagioneSociale(ecIntermediarioPT.getIntermediarioPTRagioneSociale());
        }
        return ecIntermediarioPTModel;
    }

    public static List<ECIntermediarioPT> convertModelsToDtos(List<ECIntermediarioPTModel> ecIntermediarioPTModels) {
        List<ECIntermediarioPT> ecIntermediarioPTs = null;
        if(ecIntermediarioPTModels!= null){
            ecIntermediarioPTs = ecIntermediarioPTModels.stream().map(ECIntermediaroPTMapper::convertModelToDto).collect(java.util.stream.Collectors.toList());
        }
        return ecIntermediarioPTs;
    }

    public static List<ECIntermediarioPTModel> convertDtoToModels(List<ECIntermediarioPT> ecIntermediarioPTs) {
        List<ECIntermediarioPTModel> ecIntermediarioPTModels = null;
        if(ecIntermediarioPTs!= null){
            ecIntermediarioPTModels = ecIntermediarioPTs.stream().map(ECIntermediaroPTMapper::convertDtoToModel).collect(java.util.stream.Collectors.toList());
        }
        return ecIntermediarioPTModels;
    }
}
