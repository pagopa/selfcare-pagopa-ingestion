package it.pagopa.selfcare.pagopa.injestion.api.dao.impl;

import it.pagopa.selfcare.pagopa.injestion.api.dao.mapper.PTMapper;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.PTEntity;
import it.pagopa.selfcare.pagopa.injestion.api.dao.repo.PTRepository;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.PTConnector;
import it.pagopa.selfcare.pagopa.injestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.injestion.model.dto.PT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PTConnectorImpl implements PTConnector {

    private final PTRepository repository;

    public PTConnectorImpl(PTRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PT> findAllByStatus(int page, int pageSize, String status) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<PT> pts = repository.findAllByStatus(status, pageRequest)
                .stream()
                .map(PTMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi PT con workStatus '{}'", pts.size(), status);
        return pts;
    }

    @Override
    public PT save(PT pt) {
        PTEntity entity = PTMapper.dtoToEntity(pt);
        PTEntity savedEntity = repository.save(entity);
        log.info("Salvato PT con id: {}", savedEntity.getId());
        return PTMapper.entityToDto(savedEntity);
    }
}
