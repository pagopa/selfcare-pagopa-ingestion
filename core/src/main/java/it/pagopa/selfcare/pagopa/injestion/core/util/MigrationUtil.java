package it.pagopa.selfcare.pagopa.injestion.core.util;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.commons.base.utils.Origin;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.commons.base.utils.ProductId.PROD_PAGOPA;

@RequiredArgsConstructor(access = AccessLevel.NONE)
public class MigrationUtil {

    private static BillingData fillBillingDataFromInstitutionAndEC(EC ec) {
        BillingData billingData = new BillingData();
        billingData.setBusinessName(ec.getBusinessName());

        if(Origin.INFOCAMERE != ec.getOrigin()){
            billingData.setDigitalAddress(ec.getDigitalAddress());
        }

        billingData.setRecipientCode(ec.getRecipientCode());
        billingData.setRegisteredOffice(ec.getRegisteredOffice());
        billingData.setTaxCode(ec.getTaxCode());
        billingData.setZipCode(ec.getZipCode());
        billingData.setVatNumber(ec.getVatNumber());
        return billingData;
    }

    private static BillingData fillBillingDataFromInstitutionAndEC(PT pt) {
        BillingData billingData = new BillingData();
        billingData.setBusinessName(pt.getBusinessName());

        if(Origin.INFOCAMERE != pt.getOrigin()){
            billingData.setDigitalAddress(pt.getDigitalAddress());
        }

        billingData.setRecipientCode(pt.getRecipientCode());
        billingData.setRegisteredOffice(pt.getRegisteredOffice());
        billingData.setTaxCode(pt.getTaxCode());
        billingData.setZipCode(pt.getZipCode());
        billingData.setVatNumber(pt.getVatNumber());
        return billingData;
    }

    public static AutoApprovalOnboarding constructOnboardingDto(EC ec, List<User> users) {
        AutoApprovalOnboarding onboarding = new AutoApprovalOnboarding();
        List<UserToOnboard> userToOnboards = new ArrayList<>();
        onboarding.setBillingData(fillBillingDataFromInstitutionAndEC(ec));
        onboarding.setInstitutionType(InstitutionType.PA);
        onboarding.setOrigin(ec.getOrigin());
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
        List<UserToOnboard> userToOnboards = new ArrayList<>();

        onboarding.setBillingData(fillBillingDataFromInstitutionAndEC(pt));
        onboarding.setInstitutionType(InstitutionType.PA);
        onboarding.setOrigin(pt.getOrigin());
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
