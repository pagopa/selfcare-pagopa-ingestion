package it.pagopa.selfcare.pagopa.ingestion.api.dao.mapper;

import it.pagopa.selfcare.pagopa.ingestion.api.dao.model.PTEntity;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.PT;
import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
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
        pt.setOnboardingMessage(entity.getOnboardingMessage());
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
        entity.setOnboardingMessage(pt.getOnboardingMessage());
        return entity;
    }

}