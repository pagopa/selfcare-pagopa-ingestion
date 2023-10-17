package it.pagopa.selfcare.pagopa.injestion.api.dao.mapper;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.ECEntity;
import it.pagopa.selfcare.pagopa.injestion.model.dto.EC;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Status;

import java.util.List;
import java.util.stream.Collectors;

public class ECMapper {

    public static EC entityToDto(ECEntity entity) {
        if (entity == null) {
            return null;
        }

        EC ec = new EC();
        ec.setBusinessName(entity.getBusinessName());
        ec.setRecipientCode(entity.getRecipientCode());
        ec.setDigitalAddress(entity.getDigitalAddress());
        ec.setTaxCode(entity.getTaxCode());
        ec.setVatNumber(entity.getVatNumber());
        ec.setZipCode(entity.getZipCode());
        ec.setRegisteredOffice(entity.getRegisteredOffice());

        return ec;
    }

    public static ECEntity dtoToEntity(EC ec) {
        if (ec == null) {
            return null;
        }

        ECEntity entity = new ECEntity();
        entity.setBusinessName(ec.getBusinessName());
        entity.setRecipientCode(ec.getRecipientCode());
        entity.setDigitalAddress(ec.getDigitalAddress());
        entity.setTaxCode(ec.getTaxCode());
        entity.setVatNumber(ec.getVatNumber());
        entity.setZipCode(ec.getZipCode());
        entity.setRegisteredOffice(ec.getRegisteredOffice());
        entity.setStatus(ec.getStatus() == null ? Status.TO_WORK.name() : ec.getStatus().name());

        return entity;
    }

    public static List<ECEntity> dtoListToEntityList(List<EC> ecList) {
        return ecList.stream()
                .map(ECMapper::dtoToEntity)
                .collect(Collectors.toList());
    }

    public static List<EC> entityListToDtoList(List<ECEntity> ecEntityList) {
        return ecEntityList.stream()
                .map(ECMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
