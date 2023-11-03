package it.pagopa.selfcare.pagopa.ingestion.mapper;

import it.pagopa.selfcare.pagopa.ingestion.model.csv.UserModel;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.Role;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public class UserMapper {

    public static User convertModelToDto(UserModel userModel) {
        User user = null;
        if(userModel != null){
            user = new User();
            user.setCorrelationId(userModel.getTaxCode() + "#" + userModel.getInstitutionTaxCode());
            user.setTaxCode(userModel.getTaxCode());
            user.setInstitutionTaxCode(userModel.getInstitutionTaxCode());
            user.setName(userModel.getName());
            user.setSurname(userModel.getSurname());
            if(!StringUtils.isBlank(userModel.getRole())) {
                user.setRole(Role.valueOf(userModel.getRole()));
            }
            user.setStatus(userModel.getStatus());
            user.setEmail(userModel.getEmail());
        }
        return user;
    }
}
