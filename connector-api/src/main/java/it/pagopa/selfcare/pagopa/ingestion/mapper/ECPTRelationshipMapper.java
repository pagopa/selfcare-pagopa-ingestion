package it.pagopa.selfcare.pagopa.ingestion.mapper;


import it.pagopa.selfcare.pagopa.ingestion.model.csv.ECPTRelationshipModel;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.ECPTRelationship;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public class ECPTRelationshipMapper {

    public static ECPTRelationship convertModelToDto(ECPTRelationshipModel ecptRelationshipModel) {
        ECPTRelationship ecIntermediaroPT = null;
        if(ecptRelationshipModel != null){
            ecIntermediaroPT = new ECPTRelationship();
            ecIntermediaroPT.setCorrelationId(ecptRelationshipModel.getEnteIndirettoCF() + "#" + ecptRelationshipModel.getIntermediarioPTCF());
            ecIntermediaroPT.setEnteIndirettoRagioneSociale(ecptRelationshipModel.getEnteIndirettoRagioneSociale());
            ecIntermediaroPT.setIntermediarioPTCF(ecptRelationshipModel.getIntermediarioPTCF());
            ecIntermediaroPT.setEnteIndirettoCF(ecptRelationshipModel.getEnteIndirettoCF());
            ecIntermediaroPT.setIntermediarioPTRagioneSociale(ecptRelationshipModel.getIntermediarioPTRagioneSociale());
        }
        return ecIntermediaroPT;
    }

    public static ECPTRelationship convertModelToDtoWithBatchId(ECPTRelationshipModel ecptRelationshipModel, String batchId) {
        ECPTRelationship ecIntermediaroPT = null;
        if(ecptRelationshipModel != null){
            ecIntermediaroPT = new ECPTRelationship();
            ecIntermediaroPT.setCorrelationId(ecptRelationshipModel.getEnteIndirettoCF() + "#" + ecptRelationshipModel.getIntermediarioPTCF());
            ecIntermediaroPT.setEnteIndirettoRagioneSociale(ecptRelationshipModel.getEnteIndirettoRagioneSociale());
            ecIntermediaroPT.setIntermediarioPTCF(ecptRelationshipModel.getIntermediarioPTCF());
            ecIntermediaroPT.setEnteIndirettoCF(ecptRelationshipModel.getEnteIndirettoCF());
            ecIntermediaroPT.setIntermediarioPTRagioneSociale(ecptRelationshipModel.getIntermediarioPTRagioneSociale());
            ecIntermediaroPT.setBatchId(batchId);
        }
        return ecIntermediaroPT;
    }
}
