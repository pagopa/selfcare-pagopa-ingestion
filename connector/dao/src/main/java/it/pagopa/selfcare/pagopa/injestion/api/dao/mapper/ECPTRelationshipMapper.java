package it.pagopa.selfcare.pagopa.injestion.api.dao.mapper;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.ECPTRelationshipEntity;
import it.pagopa.selfcare.pagopa.injestion.model.dto.ECPTRelationship;
import it.pagopa.selfcare.pagopa.injestion.constant.WorkStatus;

import java.util.List;
import java.util.stream.Collectors;

public class ECPTRelationshipMapper {

    public static ECPTRelationship entityToDto(ECPTRelationshipEntity entity) {
        if (entity == null) {
            return null;
        }

        ECPTRelationship ecptRelationship = new ECPTRelationship();
        ecptRelationship.setId(entity.getId());
        ecptRelationship.setIntermediarioPTCF(entity.getIntermediarioPTCF());
        ecptRelationship.setIntermediarioPTRagioneSociale(entity.getIntermediarioPTRagioneSociale());
        ecptRelationship.setEnteIndirettoCF(entity.getEnteIndirettoCF());
        ecptRelationship.setEnteIndirettoRagioneSociale(entity.getEnteIndirettoRagioneSociale());
        ecptRelationship.setWorkStatus(entity.getWorkStatus() == null ? null : WorkStatus.fromValue(entity.getWorkStatus()));
        ecptRelationship.setRetry(entity.getRetry());

        return ecptRelationship;
    }

    public static ECPTRelationshipEntity dtoToEntity(ECPTRelationship ecptRelationship) {
        if (ecptRelationship == null) {
            return null;
        }

        ECPTRelationshipEntity entity = new ECPTRelationshipEntity();
        entity.setId(ecptRelationship.getId());
        entity.setEnteIndirettoCF(ecptRelationship.getEnteIndirettoCF());
        entity.setIntermediarioPTCF(ecptRelationship.getIntermediarioPTCF());
        entity.setIntermediarioPTRagioneSociale(ecptRelationship.getIntermediarioPTRagioneSociale());
        entity.setEnteIndirettoRagioneSociale(ecptRelationship.getEnteIndirettoRagioneSociale());
        entity.setWorkStatus(ecptRelationship.getWorkStatus() == null ? WorkStatus.NOT_WORKED.name() : ecptRelationship.getWorkStatus().name());
        entity.setRetry(ecptRelationship.getRetry());

        return entity;
    }

    public static List<ECPTRelationship> entitiesToDto(List<ECPTRelationshipEntity> entities) {
        return entities.stream()
                .map(ECPTRelationshipMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public static List<ECPTRelationshipEntity> convertDtoToEntities(List<ECPTRelationship> ecptRelationships) {
        return ecptRelationships.stream()
                .map(ECPTRelationshipMapper::dtoToEntity)
                .collect(Collectors.toList());
    }

}
