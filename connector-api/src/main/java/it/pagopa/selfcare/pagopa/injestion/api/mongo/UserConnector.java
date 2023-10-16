package it.pagopa.selfcare.pagopa.injestion.api.mongo;

import it.pagopa.selfcare.pagopa.injestion.model.dto.User;

import java.util.List;
import java.util.Optional;

public interface UserConnector {

    List<User> findAll(int page, int pageSize);

    List<User> findAll();

    User findById(String id);

    Optional<User> findByTaxCode(String taxCode);

    List<User> findAllByTaxCode(String taxCode);

    User save(User user);

    List<User> saveAll(List<User> users);

    void deleteById(String id);

    void delete(User user);
    
}
