package it.pagopa.selfcare.pagopa.injestion.connector;

import it.pagopa.selfcare.pagopa.injestion.dto.ECIntermediarioPT;

import java.util.List;

public interface ECIntermediarioPTConnector {

    List<ECIntermediarioPT> findAll(int page, int pageSize);

    List<ECIntermediarioPT> findAll();

    ECIntermediarioPT findById(String id);

    ECIntermediarioPT save(ECIntermediarioPT ecIntermediarioPT);

    List<ECIntermediarioPT> saveAll(List<ECIntermediarioPT> ecIntermediarioPTList);

    void deleteById(String id);

    void delete(ECIntermediarioPT ecIntermediarioPT);

}
