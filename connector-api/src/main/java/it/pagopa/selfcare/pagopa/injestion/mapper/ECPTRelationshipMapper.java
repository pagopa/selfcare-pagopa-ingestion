package it.pagopa.selfcare.pagopa.injestion.mapper;


import it.pagopa.selfcare.pagopa.injestion.model.csv.ECPTRelationshipModel;
import it.pagopa.selfcare.pagopa.injestion.model.dto.ECPTRelationship;

import java.util.List;

public class ECPTRelationshipMapper {

    public static ECPTRelationship convertModelToDto(ECPTRelationshipModel ecptRelationshipModel) {
        ECPTRelationship ecIntermediaroPT = null;
        if(ecptRelationshipModel != null){
            ecIntermediaroPT = new ECPTRelationship();
            ecIntermediaroPT.setEnteIndirettoRagioneSociale(ecptRelationshipModel.getEnteIndirettoRagioneSociale());
            ecIntermediaroPT.setIntermediarioPTCF(ecptRelationshipModel.getIntermediarioPTCF());
            ecIntermediaroPT.setEnteIndirettoCF(ecptRelationshipModel.getEnteIndirettoCF());
            ecIntermediaroPT.setIntermediarioPTRagioneSociale(ecptRelationshipModel.getIntermediarioPTRagioneSociale());
            ecIntermediaroPT.setRetry(0);
        }
        return ecIntermediaroPT;
    }

    public static ECPTRelationshipModel convertDtoToModel(ECPTRelationship ecptRelationship) {
        ECPTRelationshipModel ecptRelationshipModel = null;
        if(ecptRelationship != null){
            ecptRelationshipModel = new ECPTRelationshipModel();
            ecptRelationshipModel.setEnteIndirettoRagioneSociale(ecptRelationship.getEnteIndirettoRagioneSociale());
            ecptRelationshipModel.setIntermediarioPTCF(ecptRelationship.getIntermediarioPTCF());
            ecptRelationshipModel.setEnteIndirettoCF(ecptRelationship.getEnteIndirettoCF());
            ecptRelationshipModel.setIntermediarioPTRagioneSociale(ecptRelationship.getIntermediarioPTRagioneSociale());
        }
        return ecptRelationshipModel;
    }

    public static List<ECPTRelationship> convertModelsToDtos(List<ECPTRelationshipModel> ecptRelationshipModels) {
        List<ECPTRelationship> ecptRelationships = null;
        if(ecptRelationshipModels != null){
            ecptRelationships = ecptRelationshipModels.stream().map(ECPTRelationshipMapper::convertModelToDto).collect(java.util.stream.Collectors.toList());
        }
        return ecptRelationships;
    }

    public static List<ECPTRelationshipModel> convertDtoToModels(List<ECPTRelationship> ecptRelationships) {
        List<ECPTRelationshipModel> ecptRelationshipModels = null;
        if(ecptRelationships != null){
            ecptRelationshipModels = ecptRelationships.stream().map(ECPTRelationshipMapper::convertDtoToModel).collect(java.util.stream.Collectors.toList());
        }
        return ecptRelationshipModels;
    }
}
