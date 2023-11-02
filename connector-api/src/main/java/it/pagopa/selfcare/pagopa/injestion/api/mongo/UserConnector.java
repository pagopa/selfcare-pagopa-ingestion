package it.pagopa.selfcare.pagopa.injestion.api.mongo;

import it.pagopa.selfcare.pagopa.injestion.model.dto.Role;
import it.pagopa.selfcare.pagopa.injestion.model.dto.User;

import java.util.List;

public interface UserConnector {

    User findManagerByInstitutionTaxCodeAndRole(String taxCode, Role role);

    User save(User user);

    List<User> saveAll(List<User> users);

    List<User> findAllByStatus(int page, int pageSize, String status);
}
