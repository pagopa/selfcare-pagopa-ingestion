package it.pagopa.selfcare.pagopa.injestion.connector.dao.mapper;

import it.pagopa.selfcare.pagopa.injestion.connector.dao.model.PTEntity;
import it.pagopa.selfcare.pagopa.injestion.dto.PT;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class PTMapper {

    public static PT entityToDto(PTEntity entity) {
        if (entity == null) {
            return null;
        }

        PT pt = new PT();
        pt.setTaxCode(entity.getTaxCode());
        pt.setZipCode(entity.getZipCode());
        pt.setDigitalAddress(entity.getDigitalAddress());
        pt.setBusinessName(entity.getBusinessName());
        pt.setVatNumber(entity.getVatNumber());
        pt.setRegisteredOffice(entity.getRegisteredOffice());

        return pt;
    }

    public static PTEntity dtoToEntity(PT pt) {
        if (pt == null) {
            return null;
        }

        PTEntity entity = new PTEntity();
        entity.setTaxCode(pt.getTaxCode());
        entity.setZipCode(pt.getZipCode());
        entity.setDigitalAddress(pt.getDigitalAddress());
        entity.setBusinessName(pt.getBusinessName());
        entity.setVatNumber(pt.getVatNumber());
        entity.setRegisteredOffice(pt.getRegisteredOffice());

        return entity;
    }

    public static List<PTEntity> dtoListToEntityList(List<PT> ptList) {
        return ptList.stream()
                .map(PTMapper::dtoToEntity)
                .collect(Collectors.toList());
    }

    public static List<PT> entityListToDtoList(List<PTEntity> entityList) {
        return entityList.stream()
                .map(PTMapper::entityToDto)
                .collect(Collectors.toList());
    }

}
