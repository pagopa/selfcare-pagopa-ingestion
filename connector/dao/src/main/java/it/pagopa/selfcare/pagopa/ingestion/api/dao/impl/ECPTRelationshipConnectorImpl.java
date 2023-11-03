package it.pagopa.selfcare.pagopa.ingestion.api.dao.impl;

import it.pagopa.selfcare.pagopa.ingestion.api.dao.mapper.ECPTRelationshipMapper;
import it.pagopa.selfcare.pagopa.ingestion.api.dao.model.ECPTRelationshipEntity;
import it.pagopa.selfcare.pagopa.ingestion.api.dao.repo.ECPTRelationshipRepository;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.ECPTRelationshipConnector;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.ECPTRelationship;
import it.pagopa.selfcare.pagopa.ingestion.utils.MaskData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
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
        log.info("Salvato ECIntermediarioPT con correlationId: {}", MaskData.maskData(savedEntity.getCorrelationId()));
        return ECPTRelationshipMapper.entityToDto(savedEntity);
    }
}
