package it.pagopa.selfcare.pagopa.ingestion.model.dto;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.commons.base.utils.ProductId.PROD_PAGOPA;

@Data
public class OnboardingUserRequest {
    private String institutionTaxCode;
    private String productId;
    private List<UserToOnboard> users;

    public OnboardingUserRequest(String institutionTaxCode, List<User> users) {
        this.institutionTaxCode = institutionTaxCode;
        this.users = toUserToOnboard(users);
        productId = PROD_PAGOPA.getValue();
    }

    private List<UserToOnboard> toUserToOnboard(List<User> users) {
        return users.stream().map(user -> {
            UserToOnboard userToOnboard = new UserToOnboard();
            userToOnboard.setName(user.getName());
            userToOnboard.setSurname(user.getSurname());
            userToOnboard.setEmail(user.getEmail());
            userToOnboard.setTaxCode(user.getTaxCode());
            userToOnboard.setRole(user.getRole() == Role.RP ? PartyRole.MANAGER : PartyRole.OPERATOR);
            return userToOnboard;
        }).collect(Collectors.toList());
    }
}
