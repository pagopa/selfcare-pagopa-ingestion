package it.pagopa.selfcare.pagopa.injestion.api.dao.impl;

import it.pagopa.selfcare.pagopa.injestion.api.dao.mapper.ECPTRelationshipMapper;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.ECPTRelationshipEntity;
import it.pagopa.selfcare.pagopa.injestion.api.dao.repo.ECPTRelationshipRepository;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.ECPTRelationshipConnector;
import it.pagopa.selfcare.pagopa.injestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.injestion.model.dto.ECPTRelationship;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ECPTRelationshipConnectorImpl implements ECPTRelationshipConnector {

    private final ECPTRelationshipRepository repository;

    public ECPTRelationshipConnectorImpl(ECPTRelationshipRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ECPTRelationship> findAllByStatus(int page, int pageSize, String status) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<ECPTRelationship> ecptRelationships = repository.findAllByStatus(status, pageRequest)
                .stream()
                .map(ECPTRelationshipMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi ECPTRelationship con workStatus '{}'", ecptRelationships.size(), status);
        return ecptRelationships;
    }

    @Override
    public ECPTRelationship save(ECPTRelationship ecptRelationship) {
        final ECPTRelationshipEntity entity = ECPTRelationshipMapper.dtoToEntity(ecptRelationship);
        ECPTRelationshipEntity savedEntity = repository.save(entity);
        log.info("Salvato ECIntermediarioPT con id: {}", savedEntity.getId());
        return ECPTRelationshipMapper.entityToDto(savedEntity);
    }
}
