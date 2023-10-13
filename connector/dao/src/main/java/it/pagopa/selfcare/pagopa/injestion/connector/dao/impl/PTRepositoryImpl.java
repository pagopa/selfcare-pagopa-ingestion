package it.pagopa.selfcare.pagopa.injestion.connector.dao.impl;

import it.pagopa.selfcare.pagopa.injestion.connector.PTConnector;
import it.pagopa.selfcare.pagopa.injestion.connector.dao.mapper.PTMapper;
import it.pagopa.selfcare.pagopa.injestion.connector.dao.model.PTEntity;
import it.pagopa.selfcare.pagopa.injestion.connector.dao.repo.PTRepository;
import it.pagopa.selfcare.pagopa.injestion.dto.PT;
import it.pagopa.selfcare.pagopa.injestion.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PTRepositoryImpl implements PTConnector {

    private final PTRepository repository;

    public PTRepositoryImpl(PTRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PT> findAll(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<PT> ptList = repository.findAll(pageRequest).stream()
                .map(PTMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi PT paginati (pagina {}, dimensione {})", ptList.size(), page, pageSize);
        return ptList;
    }

    @Override
    public List<PT> findAll() {
        List<PT> ptList = repository.findAll().stream()
                .map(PTMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi PT", ptList.size());
        return ptList;
    }

    @Override
    public PT findById(String id) {
        return repository.findById(id)
                .map(entity -> {
                    log.info("Trovato PT con id: {}", entity.getId());
                    return PTMapper.entityToDto(entity);
                })
                .orElseThrow(() -> {
                    log.error("PT non trovato con id: {}", id);
                    return new ResourceNotFoundException("PT NOT FOUND " + id);
                });
    }

    @Override
    public PT save(PT pt) {
        final PTEntity entity = PTMapper.dtoToEntity(pt);
        PTEntity savedEntity = repository.save(entity);
        log.info("Salvato PT con id: {}", savedEntity.getId());
        return PTMapper.entityToDto(savedEntity);
    }

    @Override
    public List<PT> saveAll(List<PT> ptList) {
        List<PT> savedPtList = ptList.stream()
                .map(this::save)
                .collect(Collectors.toList());
        log.info("Salvati {} elementi PT", savedPtList.size());
        return savedPtList;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
        log.info("Eliminato PT con id: {}", id);
    }

    @Override
    public void delete(PT pt) {
        PTEntity entity = PTMapper.dtoToEntity(pt);
        repository.delete(entity);
        log.info("Eliminato PT con id: {}", entity.getId());
    }

}
