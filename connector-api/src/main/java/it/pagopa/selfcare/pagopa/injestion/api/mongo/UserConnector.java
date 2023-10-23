package it.pagopa.selfcare.pagopa.injestion.api.mongo;

import it.pagopa.selfcare.pagopa.injestion.model.dto.User;

import java.util.List;

public interface UserConnector {

    List<User> findAllByInstitutionTaxCode(String taxCode);

    User save(User user);

    List<User> saveAll(List<User> users);
    
}
