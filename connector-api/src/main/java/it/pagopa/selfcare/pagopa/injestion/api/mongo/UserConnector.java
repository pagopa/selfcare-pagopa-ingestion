package it.pagopa.selfcare.pagopa.injestion.api.mongo;

import it.pagopa.selfcare.pagopa.injestion.model.User;

import java.util.List;

public interface UserConnector {

    List<User> findAll(int page, int pageSize);

    List<User> findAll();

    User findById(String id);

    User save(User user);

    List<User> saveAll(List<User> users);

    void deleteById(String id);

    void delete(User user);
    
}
