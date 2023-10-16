package it.pagopa.selfcare.pagopa.injestion.core.mapper;


import it.pagopa.selfcare.pagopa.injestion.model.Role;
import it.pagopa.selfcare.pagopa.injestion.model.User;
import it.pagopa.selfcare.pagopa.injestion.model.csv.UserModel;

import java.util.List;

public class UserMapper {

    public static User convertModelToDto(UserModel userModel) {
        User user = null;
        if(userModel != null){
            user = new User();
            user.setInstitutionTaxCode(userModel.getInstitutionTaxCode());
            user.setName(userModel.getName());
            user.setSurname(userModel.getSurname());
            user.setRole(Role.valueOf(userModel.getRole()));
            user.setStatus(userModel.getStatus());
            user.setEmail(userModel.getEmail());
            user.setTaxCode(userModel.getTaxCode());
        }
        return user;
    }

    public static UserModel convertDtoToModel(User user) {
        UserModel userModel = null;
        if(user!= null){
            userModel = new UserModel();
            userModel.setInstitutionTaxCode(user.getInstitutionTaxCode());
            userModel.setName(user.getName());
            userModel.setSurname(user.getSurname());
            userModel.setRole(user.getRole().name());
            userModel.setStatus(user.getStatus());
            userModel.setEmail(user.getEmail());
            userModel.setTaxCode(user.getTaxCode());
        }
        return userModel;
    }

    public static List<User> convertModelsToDto(List<UserModel> userModels) {
        List<User> users = null;
        if(userModels!= null){
            users = userModels.stream().map(UserMapper::convertModelToDto).collect(java.util.stream.Collectors.toList());
        }
        return users;
    }

    public static List<UserModel> convertDtoToModels(List<User> users) {
        List<UserModel> userModels = null;
        if(users!= null){
            userModels = users.stream().map(UserMapper::convertDtoToModel).collect(java.util.stream.Collectors.toList());
        }
        return userModels;
    }

}
