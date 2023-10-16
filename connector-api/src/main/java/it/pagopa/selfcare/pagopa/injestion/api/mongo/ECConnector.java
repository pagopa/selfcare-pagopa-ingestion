package it.pagopa.selfcare.pagopa.injestion.api.mongo;

import it.pagopa.selfcare.pagopa.injestion.model.EC;

import java.util.List;

public interface ECConnector {

    List<EC> findAll(int page, int pageSize);

    List<EC> findAll();

    EC findById(String id);

    EC save(EC ec);

    List<EC> saveAll(List<EC> ecList);

    void deleteById(String id);

    void delete(EC ec);
}