package it.pagopa.selfcare.pagopa.injestion.api.dao.mapper;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.UserEntity;
import it.pagopa.selfcare.pagopa.injestion.model.Role;
import it.pagopa.selfcare.pagopa.injestion.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class UserMapper {

    public static User entityToDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User();
        user.setEmail(entity.getEmail());
        user.setName(entity.getName());
        user.setRole(Role.valueOf(entity.getRole()));
        user.setSurname(entity.getSurname());
        user.setStatus(entity.getStatus());
        user.setInstitutionTaxCode(entity.getInstitutionTaxCode());

        return user;
    }

    public static UserEntity dtoToEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());
        entity.setRole(user.getRole().name());
        entity.setSurname(user.getSurname());
        entity.setStatus(user.getStatus());
        entity.setInstitutionTaxCode(user.getInstitutionTaxCode());

        return entity;
    }

    public List<UserEntity> dtoListToEntityList(List<User> userList) {
        return userList.stream()
                .map(UserMapper::dtoToEntity)
                .collect(Collectors.toList());
    }

    public List<User> entityListToDtoList(List<UserEntity> userEntityList) {
        return userEntityList.stream()
                .map(UserMapper::entityToDto)
                .collect(Collectors.toList());
    }

}
