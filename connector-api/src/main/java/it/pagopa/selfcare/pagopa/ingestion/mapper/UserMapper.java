package it.pagopa.selfcare.pagopa.ingestion.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.pagopa.ingestion.constant.WorkStatus;
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
            boolean isPtUser = !StringUtils.isEmpty(userModel.getPtTaxCode());
            user.setInstitutionTaxCode(isPtUser ? userModel.getPtTaxCode() : userModel.getInstitutionTaxCode());
            user.setTaxCode(!StringUtils.isEmpty(userModel.getTaxCode()) ? userModel.getTaxCode() : "NO_TAXCODE");
            user.setCorrelationId(user.getTaxCode() + "#" + user.getInstitutionTaxCode());

            user.setName(userModel.getName());
            user.setSurname(userModel.getSurname());
            if(!StringUtils.isBlank(userModel.getRole())) {
                // Ruolo dal csv
                user.setRole(Role.fromValue(userModel.getRole()));
                // Ruoli su ms-core
                user.setOnboardingRole(isPtUser ? getPtOnboardingRole(user.getRole()) : getEcOnboardingRole(user.getRole()));
                user.setOnboardingProductRole(user.getOnboardingRole() == PartyRole.MANAGER ? "admin" : "operator");
            }
            user.setWorkStatus(user.getTaxCode().equalsIgnoreCase("NO_TAXCODE") ? WorkStatus.EMPTY_USER_CF : null);
            user.setEmail(userModel.getEmail());
        }
        return user;
    }

    public static User convertModelToDtoWithBatchId(UserModel userModel, String batchId) {
        User user = null;
        if(userModel != null){
            user = new User();
            boolean isPtUser = !StringUtils.isEmpty(userModel.getPtTaxCode());
            user.setInstitutionTaxCode(isPtUser ? userModel.getPtTaxCode() : userModel.getInstitutionTaxCode());
            user.setTaxCode(!StringUtils.isEmpty(userModel.getTaxCode()) ? userModel.getTaxCode() : "NO_TAXCODE");
            user.setCorrelationId(user.getTaxCode() + "#" + user.getInstitutionTaxCode());

            user.setName(userModel.getName());
            user.setSurname(userModel.getSurname());
            if(!StringUtils.isBlank(userModel.getRole())) {
                // Ruolo dal csv
                user.setRole(Role.fromValue(userModel.getRole()));
                // Ruoli su ms-core
                user.setOnboardingRole(isPtUser ? getPtOnboardingRole(user.getRole()) : getEcOnboardingRole(user.getRole()));
                user.setOnboardingProductRole(user.getOnboardingRole() == PartyRole.MANAGER ? "admin" : "operator");
            }
            user.setWorkStatus(user.getTaxCode().equalsIgnoreCase("NO_TAXCODE") ? WorkStatus.EMPTY_USER_CF : null);
            user.setEmail(userModel.getEmail());
            user.setBatchId(batchId);
        }
        return user;
    }

    private static PartyRole getEcOnboardingRole(Role role) {
        return role == Role.RP ? PartyRole.MANAGER : PartyRole.OPERATOR;
    }

    private static PartyRole getPtOnboardingRole(Role role) {
         return role == Role.RT ? PartyRole.MANAGER : PartyRole.OPERATOR;
    }
}
