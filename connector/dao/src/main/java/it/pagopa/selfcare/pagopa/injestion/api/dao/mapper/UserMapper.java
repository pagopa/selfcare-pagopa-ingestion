package it.pagopa.selfcare.pagopa.injestion.api.dao.mapper;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.UserEntity;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Role;
import it.pagopa.selfcare.pagopa.injestion.model.dto.User;
import it.pagopa.selfcare.pagopa.injestion.constant.WorkStatus;

public class UserMapper {

    public static User entityToDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User();
        user.setEmail(entity.getEmail());
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setTaxCode(entity.getTaxCode());
        if(entity.getRole() != null) {
            user.setRole(Role.valueOf(entity.getRole()));
        }
        user.setSurname(entity.getSurname());
        user.setStatus(entity.getStatus());
        user.setInstitutionTaxCode(entity.getInstitutionTaxCode());
        user.setWorkStatus(entity.getWorkStatus() == null ? null : WorkStatus.fromValue(entity.getWorkStatus()));

        return user;
    }

    public static UserEntity dtoToEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
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
        return entity;
    }
}
