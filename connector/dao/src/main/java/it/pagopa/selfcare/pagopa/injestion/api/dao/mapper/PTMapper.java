package it.pagopa.selfcare.pagopa.injestion.api.dao.mapper;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.PTEntity;
import it.pagopa.selfcare.pagopa.injestion.model.dto.PT;
import it.pagopa.selfcare.pagopa.injestion.model.dto.WorkStatus;

import java.util.List;
import java.util.stream.Collectors;

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
        pt.setWorkStatus(entity.getWorkStatus() == null ? null : WorkStatus.fromValue(entity.getWorkStatus()));

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
        entity.setWorkStatus(pt.getWorkStatus() == null ? WorkStatus.NOT_WORKED.name() : pt.getWorkStatus().name());

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
