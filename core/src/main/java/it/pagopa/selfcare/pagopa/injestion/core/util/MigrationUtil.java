package it.pagopa.selfcare.pagopa.injestion.core.util;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.commons.base.utils.ProductId.PROD_PAGOPA;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public class MigrationUtil {


    public static AutoApprovalOnboarding constructOnboardingDto(EC ec, List<User> users) {
        AutoApprovalOnboarding onboarding = new AutoApprovalOnboarding();
        onboarding.setTaxCode(ec.getTaxCode());
        onboarding.setVatNumber(ec.getVatNumber());
        onboarding.setRecipientCode(ec.getRecipientCode());
        onboarding.setBusinessName(ec.getBusinessName());
        onboarding.setProductId(PROD_PAGOPA.getValue());

        List<UserToOnboard> userToOnboards = new ArrayList<>();
        if(!CollectionUtils.isEmpty(users)) {
            userToOnboards = users.stream()
                    .map(user -> {
                        UserToOnboard userToSend = new UserToOnboard();
                        userToSend.setTaxCode(user.getTaxCode());
                        userToSend.setName(user.getName());
                        userToSend.setSurname(user.getSurname());
                        userToSend.setEmail(user.getEmail());
                        userToSend.setRole(user.getRole() == Role.RP ? PartyRole.MANAGER : PartyRole.OPERATOR);
                        return userToSend;
                    }).collect(Collectors.toList());
        }
        onboarding.setUsers(userToOnboards);

        return onboarding;
    }

    public static AutoApprovalOnboarding constructOnboardingDto(PT pt, List<User> users) {
        AutoApprovalOnboarding onboarding = new AutoApprovalOnboarding();
        onboarding.setTaxCode(pt.getTaxCode());
        onboarding.setVatNumber(pt.getVatNumber());
        onboarding.setRecipientCode(pt.getRecipientCode());
        onboarding.setBusinessName(pt.getBusinessName());
        onboarding.setProductId(PROD_PAGOPA.getValue());

        List<UserToOnboard> userToOnboards = new ArrayList<>();
        if(!CollectionUtils.isEmpty(users)) {
            userToOnboards = users.stream()
                    .map(user -> {
                        UserToOnboard userToSend = new UserToOnboard();
                        userToSend.setTaxCode(user.getTaxCode());
                        userToSend.setName(user.getName());
                        userToSend.setSurname(user.getSurname());
                        userToSend.setEmail(user.getEmail());
                        userToSend.setRole(user.getRole() == Role.RP ? PartyRole.MANAGER : PartyRole.OPERATOR);
                        return userToSend;
                    }).collect(Collectors.toList());
        }
        onboarding.setUsers(userToOnboards);

        return onboarding;
    }

    public static Delegation createDelegation(ECPTRelationship ecptRelationship) {
        Delegation delegation = new Delegation();
        delegation.setFrom(ecptRelationship.getEnteIndirettoCF());
        delegation.setTo(ecptRelationship.getIntermediarioPTCF());
        delegation.setInstitutionFromName(ecptRelationship.getEnteIndirettoRagioneSociale());
        delegation.setInstitutionToName(ecptRelationship.getEnteIndirettoRagioneSociale());
        delegation.setProductId(PROD_PAGOPA.getValue());
        delegation.setType(DelegationType.PT);
        return delegation;
    }

}
