package it.pagopa.selfcare.pagopa.injestion.api.dao.impl;

import it.pagopa.selfcare.pagopa.injestion.api.mongo.ECIntermediarioPTConnector;
import it.pagopa.selfcare.pagopa.injestion.api.dao.mapper.ECIntermediarioPTMapper;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.ECIntermediarioPTEntity;
import it.pagopa.selfcare.pagopa.injestion.api.dao.repo.ECIntermediarioPTRepository;
import it.pagopa.selfcare.pagopa.injestion.model.dto.ECIntermediarioPT;
import it.pagopa.selfcare.pagopa.injestion.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ECIntermediarioPTConnectorImpl implements ECIntermediarioPTConnector {

    private final ECIntermediarioPTRepository repository;

    public ECIntermediarioPTConnectorImpl(ECIntermediarioPTRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ECIntermediarioPT> findAll(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<ECIntermediarioPT> dtos = repository.findAll(pageRequest)
                .stream()
                .map(ECIntermediarioPTMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi ECIntermediarioPT paginati (pagina {}, dimensione {})", dtos.size(), page, pageSize);
        return dtos;
    }

    @Override
    public List<ECIntermediarioPT> findAll() {
        List<ECIntermediarioPT> dtos = repository.findAll()
                .stream()
                .map(ECIntermediarioPTMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi ECIntermediarioPT", dtos.size());
        return dtos;
    }

    @Override
    public ECIntermediarioPT findById(String id) {
        Optional<ECIntermediarioPTEntity> optionalEntity = repository.findById(id);
        ECIntermediarioPTEntity entity = optionalEntity.orElseThrow(() -> new ResourceNotFoundException("ECINTERMEDIARIOPT NOT FOUND " + id));
        log.info("Found ECIntermediarioPT with id: {}", entity.getId());
        return ECIntermediarioPTMapper.entityToDto(entity);
    }

    @Override
    public ECIntermediarioPT save(ECIntermediarioPT ecIntermediarioPT) {
        final ECIntermediarioPTEntity entity = ECIntermediarioPTMapper.dtoToEntity(ecIntermediarioPT);
        ECIntermediarioPTEntity savedEntity = repository.save(entity);
        log.info("Salvato ECIntermediarioPT con id: {}", savedEntity.getId());
        return ECIntermediarioPTMapper.entityToDto(savedEntity);
    }

    @Override
    public List<ECIntermediarioPT> saveAll(List<ECIntermediarioPT> ecIntermediarioPTList) {
        List<ECIntermediarioPT> savedDtos = ecIntermediarioPTList
                .stream()
                .map(this::save)
                .collect(Collectors.toList());
        log.info("Salvati {} elementi ECIntermediarioPT", savedDtos.size());
        return savedDtos;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
        log.info("Eliminato ECIntermediarioPT con id: {}", id);
    }

    @Override
    public void delete(ECIntermediarioPT ecIntermediarioPT) {
        ECIntermediarioPTEntity entity = ECIntermediarioPTMapper.dtoToEntity(ecIntermediarioPT);
        repository.delete(entity);
        log.info("Eliminato ECIntermediarioPT con id: {}", entity.getId());
    }
}
