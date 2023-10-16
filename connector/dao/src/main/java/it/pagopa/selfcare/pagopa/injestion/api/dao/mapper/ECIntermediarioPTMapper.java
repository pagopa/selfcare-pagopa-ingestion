package it.pagopa.selfcare.pagopa.injestion.api.dao.mapper;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.ECIntermediarioPTEntity;
import it.pagopa.selfcare.pagopa.injestion.model.ECIntermediarioPT;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)

public class ECIntermediarioPTMapper {

    public static ECIntermediarioPT entityToDto(ECIntermediarioPTEntity entity) {
        if (entity == null) {
            return null;
        }

        ECIntermediarioPT ecIntermediarioPT = new ECIntermediarioPT();
        ecIntermediarioPT.setIntermediarioPTCF(entity.getIntermediarioPTCF());
        ecIntermediarioPT.setIntermediarioPTRagioneSociale(entity.getIntermediarioPTRagioneSociale());
        ecIntermediarioPT.setEnteIndirettoCF(entity.getEnteIndirettoCF());
        ecIntermediarioPT.setEnteIndirettoRagioneSociale(entity.getEnteIndirettoRagioneSociale());

        return ecIntermediarioPT;
    }

    public static ECIntermediarioPTEntity dtoToEntity(ECIntermediarioPT ecIntermediarioPT) {
        if (ecIntermediarioPT == null) {
            return null;
        }

        ECIntermediarioPTEntity entity = new ECIntermediarioPTEntity();
        entity.setEnteIndirettoCF(ecIntermediarioPT.getEnteIndirettoCF());
        entity.setIntermediarioPTCF(ecIntermediarioPT.getIntermediarioPTCF());
        entity.setIntermediarioPTRagioneSociale(ecIntermediarioPT.getIntermediarioPTRagioneSociale());
        entity.setEnteIndirettoRagioneSociale(ecIntermediarioPT.getEnteIndirettoRagioneSociale());

        return entity;
    }

    public static List<ECIntermediarioPT> entitiesToDto(List<ECIntermediarioPTEntity> entities) {
        return entities.stream()
                .map(ECIntermediarioPTMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public static List<ECIntermediarioPTEntity> convertDtoToEntities(List<ECIntermediarioPT> ecIntermediarioPTs) {
        return ecIntermediarioPTs.stream()
                .map(ECIntermediarioPTMapper::dtoToEntity)
                .collect(Collectors.toList());
    }

}
