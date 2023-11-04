package it.pagopa.selfcare.pagopa.ingestion.api.dao.mapper;

import it.pagopa.selfcare.pagopa.ingestion.api.dao.model.ECPTRelationshipEntity;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.ECPTRelationship;
import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public class ECPTRelationshipMapper {

    public static ECPTRelationship entityToDto(ECPTRelationshipEntity entity) {
        if (entity == null) {
            return null;
        }

        ECPTRelationship ecptRelationship = new ECPTRelationship();
        ecptRelationship.setCorrelationId(entity.getCorrelationId());
        ecptRelationship.setIntermediarioPTCF(entity.getIntermediarioPTCF());
        ecptRelationship.setIntermediarioPTRagioneSociale(entity.getIntermediarioPTRagioneSociale());
        ecptRelationship.setEnteIndirettoCF(entity.getEnteIndirettoCF());
        ecptRelationship.setEnteIndirettoRagioneSociale(entity.getEnteIndirettoRagioneSociale());
        ecptRelationship.setWorkStatus(entity.getWorkStatus() == null ? null : WorkStatus.fromValue(entity.getWorkStatus()));
        ecptRelationship.setCreateHttpStatus(entity.getCreateHttpStatus());
        ecptRelationship.setCreateMessage(entity.getCreateMessage());

        return ecptRelationship;
    }

    public static ECPTRelationshipEntity dtoToEntity(ECPTRelationship ecptRelationship) {
        if (ecptRelationship == null) {
            return null;
        }

        ECPTRelationshipEntity entity = new ECPTRelationshipEntity();
        entity.setCorrelationId(ecptRelationship.getCorrelationId());
        entity.setEnteIndirettoCF(ecptRelationship.getEnteIndirettoCF());
        entity.setIntermediarioPTCF(ecptRelationship.getIntermediarioPTCF());
        entity.setIntermediarioPTRagioneSociale(ecptRelationship.getIntermediarioPTRagioneSociale());
        entity.setEnteIndirettoRagioneSociale(ecptRelationship.getEnteIndirettoRagioneSociale());
        entity.setWorkStatus(ecptRelationship.getWorkStatus() == null ? WorkStatus.NOT_WORKED.name() : ecptRelationship.getWorkStatus().name());
        entity.setCreateHttpStatus(ecptRelationship.getCreateHttpStatus());
        entity.setCreateMessage(ecptRelationship.getCreateMessage());

        return entity;
    }
}
