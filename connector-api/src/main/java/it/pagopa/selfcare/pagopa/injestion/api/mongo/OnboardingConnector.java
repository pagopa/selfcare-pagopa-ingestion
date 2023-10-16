package it.pagopa.selfcare.pagopa.injestion.api.mongo;

import it.pagopa.selfcare.pagopa.injestion.model.dto.Onboarding;

import java.util.List;

public interface OnboardingConnector {

    List<Onboarding> findAll(int page, int pageSize);

    List<Onboarding> findAllByStatus(int page, int pageSize, String status);

    List<Onboarding> findAllByStatus(String status);

    List<Onboarding> findAll();

    Onboarding findById(String id);

    Onboarding save(Onboarding onboarding);

    List<Onboarding> saveAll(List<Onboarding> onboardingList);

    void deleteById(String id);

    void delete(Onboarding onboarding);
}
