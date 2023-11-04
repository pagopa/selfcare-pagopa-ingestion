package it.pagopa.selfcare.pagopa.ingestion.api.dao.mapper;

import it.pagopa.selfcare.pagopa.ingestion.api.dao.model.ECEntity;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.EC;
import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public class ECMapper {

    public static EC entityToDto(ECEntity entity) {
        if (entity == null) {
            return null;
        }

        EC ec = new EC();
        ec.setId(entity.getId());
        ec.setBusinessName(entity.getBusinessName());
        ec.setRecipientCode(entity.getRecipientCode());
        ec.setDigitalAddress(entity.getDigitalAddress());
        ec.setTaxCode(entity.getTaxCode());
        ec.setVatNumber(entity.getVatNumber());
        ec.setZipCode(entity.getZipCode());
        ec.setRegisteredOffice(entity.getRegisteredOffice());
        ec.setWorkStatus(entity.getWorkStatus() == null ? null : WorkStatus.fromValue(entity.getWorkStatus()));
        ec.setOnboardingHttpStatus(entity.getOnboardingHttpStatus());
        ec.setOnboardingMessage(entity.getOnboardingMessage());

        return ec;
    }

    public static ECEntity dtoToEntity(EC ec) {
        if (ec == null) {
            return null;
        }

        ECEntity entity = new ECEntity();
        entity.setId(ec.getId());
        entity.setBusinessName(ec.getBusinessName());
        entity.setRecipientCode(ec.getRecipientCode());
        entity.setDigitalAddress(ec.getDigitalAddress());
        entity.setTaxCode(ec.getTaxCode());
        entity.setVatNumber(ec.getVatNumber());
        entity.setZipCode(ec.getZipCode());
        entity.setRegisteredOffice(ec.getRegisteredOffice());
        entity.setWorkStatus(ec.getWorkStatus() == null ? WorkStatus.NOT_WORKED.name() : ec.getWorkStatus().name());
        entity.setOnboardingHttpStatus(ec.getOnboardingHttpStatus());
        entity.setOnboardingMessage(ec.getOnboardingMessage());
        return entity;
    }
}
