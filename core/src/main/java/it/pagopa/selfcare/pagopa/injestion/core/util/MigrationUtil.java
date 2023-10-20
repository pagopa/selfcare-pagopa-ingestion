package it.pagopa.selfcare.pagopa.injestion.core.util;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.pagopa.injestion.model.dto.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MigrationUtil {

    public static boolean isInstitutionValid(InstitutionProxyInfo institution) {
        return institution != null &&
                StringUtils.hasText(institution.getAddress()) &&
                StringUtils.hasText(institution.getDigitalAddress()) &&
                StringUtils.hasText(institution.getZipCode());
    }

    public static boolean isLegalAddressValid(LegalAddress legalAddress) {
        return legalAddress != null &&
                StringUtils.hasText(legalAddress.getAddress()) &&
                StringUtils.hasText(legalAddress.getZip());
    }

    private static BillingData fillBillingDataFromInstitutionAndEC(String digitalAddress, String zipCode, EC ec) {
        BillingData billingData = new BillingData();
        billingData.setBusinessName(ec.getBusinessName());
        billingData.setDigitalAddress(digitalAddress);
        billingData.setRecipientCode(ec.getRecipientCode());
        billingData.setRegisteredOffice(ec.getRegisteredOffice());
        billingData.setTaxCode(ec.getTaxCode());
        billingData.setZipCode(zipCode);
        billingData.setVatNumber(ec.getVatNumber());
        return billingData;
    }

    public static AutoApprovalOnboardingRequest constructOnboardingDto(EC ec, String origin, List<User> users) {
        AutoApprovalOnboardingRequest onboarding = new AutoApprovalOnboardingRequest();
        List<UserToOnboard> userToOnboards = new ArrayList<>();
        onboarding.setBillingData(fillBillingDataFromInstitutionAndEC(ec.getDigitalAddress(), ec.getZipCode(), ec));
        onboarding.setInstitutionType(InstitutionType.PA);
        onboarding.setGeographicTaxonomies(List.of());
        onboarding.setOrigin(origin);
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
        onboarding.setAssistanceContacts(new AssistanceContacts());
        return onboarding;
    }

}
