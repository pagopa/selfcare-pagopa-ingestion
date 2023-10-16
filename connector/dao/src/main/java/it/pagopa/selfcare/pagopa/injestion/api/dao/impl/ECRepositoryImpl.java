package it.pagopa.selfcare.pagopa.injestion.api.dao.impl;

import it.pagopa.selfcare.pagopa.injestion.api.ECConnector;
import it.pagopa.selfcare.pagopa.injestion.api.dao.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.ECEntity;
import it.pagopa.selfcare.pagopa.injestion.api.dao.repo.ECRepository;
import it.pagopa.selfcare.pagopa.injestion.dto.EC;
import it.pagopa.selfcare.pagopa.injestion.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ECRepositoryImpl implements ECConnector {

    private final ECRepository repository;

    public ECRepositoryImpl(ECRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<EC> findAll(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<EC> ecs = repository.findAll(pageRequest).stream().map(ECMapper::entityToDto).collect(Collectors.toList());
        log.info("Trovati {} elementi EC paginati (pagina {}, dimensione {})", ecs.size(), page, pageSize);
        return ecs;
    }
    @Override
    public List<EC> findAll() {
        List<EC> ecs = repository.findAll().stream().map(ECMapper::entityToDto).collect(Collectors.toList());
        log.info("Trovati {} elementi EC", ecs.size());
        return ecs;
    }

    @Override
    public EC findById(String id) {
        return repository.findById(id)
                .map(entity -> {
                    log.info("Trovato EC con id: {}", entity.getId());
                    return ECMapper.entityToDto(entity);
                }).orElseThrow(() -> {
                    log.error("EC non trovato con id: {}", id);
                    return new ResourceNotFoundException("EC NOT FOUND " + id);
                });
    }

    @Override
    public EC save(EC ec) {
        final ECEntity entity = ECMapper.dtoToEntity(ec);
        ECEntity savedEntity = repository.save(entity);
        log.info("Salvato EC con id: {}", savedEntity.getId());
        return ECMapper.entityToDto(savedEntity);
    }

    @Override
    public List<EC> saveAll(List<EC> ecList) {
        List<EC> savedEcs = ecList.stream()
                .map(this::save)
                .collect(Collectors.toList());
        log.info("Salvati {} elementi EC", savedEcs.size());
        return savedEcs;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
        log.info("Eliminato EC con id: {}", id);
    }

    @Override
    public void delete(EC ec) {
        ECEntity entity = ECMapper.dtoToEntity(ec);
        repository.delete(entity);
        log.info("Eliminato EC con id: {}", entity.getId());
    }

}
