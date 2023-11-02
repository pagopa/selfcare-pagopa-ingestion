package it.pagopa.selfcare.pagopa.injestion.api.dao.mapper;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.PTEntity;
import it.pagopa.selfcare.pagopa.injestion.model.dto.PT;
import it.pagopa.selfcare.pagopa.injestion.constant.WorkStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public class PTMapper {

    public static PT entityToDto(PTEntity entity) {
        if (entity == null) {
            return null;
        }

        PT pt = new PT();
        pt.setId(entity.getId());
        pt.setTaxCode(entity.getTaxCode());
        pt.setZipCode(entity.getZipCode());
        pt.setDigitalAddress(entity.getDigitalAddress());
        pt.setBusinessName(entity.getBusinessName());
        pt.setVatNumber(entity.getVatNumber());
        pt.setOrigin(entity.getOrigin());
        pt.setRegisteredOffice(entity.getRegisteredOffice());
        pt.setWorkStatus(entity.getWorkStatus() == null ? null : WorkStatus.fromValue(entity.getWorkStatus()));
        pt.setOnboardinHttpStatus(entity.getOnboardinHttpStatus());
        return pt;
    }

    public static PTEntity dtoToEntity(PT pt) {
        if (pt == null) {
            return null;
        }

        PTEntity entity = new PTEntity();
        entity.setId(pt.getId());
        entity.setTaxCode(pt.getTaxCode());
        entity.setZipCode(pt.getZipCode());
        entity.setDigitalAddress(pt.getDigitalAddress());
        entity.setBusinessName(pt.getBusinessName());
        entity.setVatNumber(pt.getVatNumber());
        entity.setOrigin(pt.getOrigin());
        entity.setRegisteredOffice(pt.getRegisteredOffice());
        entity.setWorkStatus(pt.getWorkStatus() == null ? WorkStatus.NOT_WORKED.name() : pt.getWorkStatus().name());
        entity.setOnboardinHttpStatus(pt.getOnboardinHttpStatus());
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
