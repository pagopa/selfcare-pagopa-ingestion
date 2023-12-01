package it.pagopa.selfcare.pagopa.ingestion.api.dao.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.pagopa.ingestion.api.dao.model.UserEntity;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.Role;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.User;
import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public class UserMapper {

    public static User entityToDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User();
        user.setEmail(entity.getEmail());
        user.setCorrelationId(entity.getCorrelationId());
        user.setName(entity.getName());
        user.setTaxCode(entity.getTaxCode());
        if(entity.getRole() != null) {
            user.setRole(Role.valueOf(entity.getRole()));
        }
        user.setSurname(entity.getSurname());
        user.setStatus(entity.getStatus());
        user.setInstitutionTaxCode(entity.getInstitutionTaxCode());
        user.setWorkStatus(entity.getWorkStatus() == null ? null : WorkStatus.fromValue(entity.getWorkStatus()));
        user.setOnboardingHttpStatus(entity.getOnboardingHttpStatus());
        user.setOnboardingMessage(entity.getOnboardingMessage());
        user.setOnboardingRole(PartyRole.valueOf(entity.getOnboardingRole()));
        user.setOnboardingProductRole(entity.getOnboardingProductRole());
        if(entity.getBatchId() != null) {
            user.setBatchId(entity.getBatchId());
        }
        return user;
    }

    public static UserEntity dtoToEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setCorrelationId(user.getCorrelationId());
        entity.setTaxCode(user.getTaxCode());
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());
        if(user.getRole() != null) {
            entity.setRole(user.getRole().name());
        }
        entity.setStatus(user.getStatus());
        entity.setSurname(user.getSurname());
        entity.setInstitutionTaxCode(user.getInstitutionTaxCode());
        entity.setWorkStatus(user.getWorkStatus() == null ? WorkStatus.NOT_WORKED.name() : user.getWorkStatus().name());
        entity.setOnboardingMessage(user.getOnboardingMessage());
        entity.setOnboardingHttpStatus(user.getOnboardingHttpStatus());
        entity.setOnboardingRole(user.getOnboardingRole().toString());
        entity.setOnboardingProductRole(user.getOnboardingProductRole());
        if(user.getBatchId() != null) {
            entity.setBatchId(user.getBatchId());
        }
        return entity;
    }
}
