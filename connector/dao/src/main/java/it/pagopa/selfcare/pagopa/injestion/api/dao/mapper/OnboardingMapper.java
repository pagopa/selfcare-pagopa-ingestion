package it.pagopa.selfcare.pagopa.injestion.api.dao.mapper;

import it.pagopa.selfcare.pagopa.injestion.api.dao.model.OnboardingEntity;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.UserEntity;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.inner.AssistanceContractsEntity;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.inner.BillingDataEntity;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.inner.GeographicTaxonomiesEntity;
import it.pagopa.selfcare.pagopa.injestion.model.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class OnboardingMapper {

    public static Onboarding entityToDto(OnboardingEntity entity) {
        if (entity == null) {
            return null;
        }

        Onboarding onboarding = new Onboarding();
        onboarding.setInstitutionType(entity.getInstitutionType());
        onboarding.setOrigin(Origin.valueOf(entity.getOrigin()));
        onboarding.setStatus(Status.valueOf(entity.getStatus()));
        onboarding.setBillingData(entityToDto(entity.getBillingData()));
        onboarding.setAssistanceContracts(entityToDto(entity.getAssistanceContracts()));
        onboarding.setUsers(entityToDto(entity.getUsers()));
        onboarding.setGeographicTaxonomies(entityToDtoG(entity.getGeographicTaxonomies()));

        return onboarding;
    }

    private static List<GeographicTaxonomies> entityToDtoG(List<GeographicTaxonomiesEntity> entityList) {
        return entityList.stream()
                .map(OnboardingMapper::entityToDto)
                .collect(Collectors.toList());
    }

    private static GeographicTaxonomies entityToDto(GeographicTaxonomiesEntity entity) {
        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        return geographicTaxonomies;
    }

    private static List<User> entityToDto(List<UserEntity> entityList) {
        return entityList.stream()
                .map(OnboardingMapper::entityToDto)
                .collect(Collectors.toList());
    }

    private static User entityToDto(UserEntity entity) {
        User user = new User();
        user.setStatus(entity.getStatus());
        user.setTaxCode(entity.getTaxCode());
        user.setRole(Role.valueOf(entity.getRole()));
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setSurname(entity.getSurname());
        user.setInstitutionTaxCode(entity.getInstitutionTaxCode());
        return user;
    }

    private static AssistanceContracts entityToDto(AssistanceContractsEntity entity) {
        AssistanceContracts assistanceContracts = new AssistanceContracts();
        return assistanceContracts;
    }

    private static BillingData entityToDto(BillingDataEntity entity) {
        BillingData billingData = new BillingData();
        billingData.setDigitalAddress(entity.getDigitalAddress());
        billingData.setTaxCode(entity.getTaxCode());
        billingData.setZipCode(entity.getZipCode());
        billingData.setRegisteredOffice(entity.getRegisteredOffice());
        billingData.setBusinessName(entity.getBusinessName());
        billingData.setVatNumber(entity.getVatNumber());
        billingData.setRecipientCode(entity.getRecipientCode());
        return billingData;
    }


    public static OnboardingEntity dtoToEntity(Onboarding onboarding) {
        OnboardingEntity onboardingEntity = new OnboardingEntity();
        onboardingEntity.setInstitutionType(onboarding.getInstitutionType());
        onboardingEntity.setOrigin(onboarding.getOrigin().name());
        onboardingEntity.setStatus(onboarding.getStatus().name());
        onboardingEntity.setBillingData(dtoToEntity(onboarding.getBillingData()));
        onboardingEntity.setAssistanceContracts(dtoToEntity(onboarding.getAssistanceContracts()));
        onboardingEntity.setUsers(dtoToEntity(onboarding.getUsers()));
        onboardingEntity.setGeographicTaxonomies(dtoToEntityG(onboarding.getGeographicTaxonomies()));

        return onboardingEntity;
    }

    private static BillingDataEntity dtoToEntity(BillingData billingData) {
        BillingDataEntity billingDataEntity = new BillingDataEntity();
        billingDataEntity.setDigitalAddress(billingData.getDigitalAddress());
        billingDataEntity.setTaxCode(billingData.getTaxCode());
        billingDataEntity.setZipCode(billingData.getZipCode());
        billingDataEntity.setRegisteredOffice(billingData.getRegisteredOffice());
        billingDataEntity.setBusinessName(billingData.getBusinessName());
        billingDataEntity.setVatNumber(billingData.getVatNumber());
        billingDataEntity.setRecipientCode(billingData.getRecipientCode());
        return billingDataEntity;
    }

    private static AssistanceContractsEntity dtoToEntity(AssistanceContracts assistanceContracts) {
        AssistanceContractsEntity assistanceContractsEntity = new AssistanceContractsEntity();
        return assistanceContractsEntity;
    }

    private static List<GeographicTaxonomiesEntity> dtoToEntityG(List<GeographicTaxonomies> geographicTaxonomies) {
        return geographicTaxonomies.stream()
               .map(OnboardingMapper::dtoToEntity)
               .collect(Collectors.toList());
    }

    private static GeographicTaxonomiesEntity dtoToEntity(GeographicTaxonomies geographicTaxonomies) {
        GeographicTaxonomiesEntity geographicTaxonomiesEntity = new GeographicTaxonomiesEntity();
        return geographicTaxonomiesEntity;
    }

    private static List<UserEntity> dtoToEntity(List<User> users) {
        return users.stream()
              .map(OnboardingMapper::dtoToEntity)
              .collect(Collectors.toList());
    }

    private static UserEntity dtoToEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setStatus(user.getStatus());
        userEntity.setTaxCode(user.getTaxCode());
        userEntity.setRole(user.getRole().name());
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setSurname(user.getSurname());
        userEntity.setInstitutionTaxCode(user.getInstitutionTaxCode());
        return userEntity;
    }

}
