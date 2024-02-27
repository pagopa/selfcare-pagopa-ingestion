package it.pagopa.selfcare.pagopa.ingestion.api.dao.impl;

import it.pagopa.selfcare.pagopa.ingestion.api.dao.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.ingestion.api.dao.model.EcAdesioneEntity;
import it.pagopa.selfcare.pagopa.ingestion.api.dao.repo.ECRepository;
import it.pagopa.selfcare.pagopa.ingestion.api.dao.repo.EcAdesioneRepository;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.ECConnector;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.EcAdesioneConnector;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.EC;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.EcAdesione;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EcAdesioneConnectorImpl implements EcAdesioneConnector {

    private final EcAdesioneRepository repository;

    public EcAdesioneConnectorImpl(EcAdesioneRepository repository) {
        this.repository = repository;
    }

    @Override
    public EcAdesione save(EcAdesione ec) {
        final EcAdesioneEntity entity = ECMapper.dtoToEntity(ec);
        EcAdesioneEntity savedEntity = repository.save(entity);
        log.info("Salvato EcAdesione con taxCode: {}", savedEntity.getTaxCode());
        return ECMapper.entityToDto(savedEntity);
    }

    @Override
    public List<EcAdesione> findAllByStatus(int page, int pageSize, String status) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<EcAdesione> ecs = repository.findAllByStatus(status, pageRequest)
                .stream()
                .map(ECMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi EC con workStatus '{}'", ecs.size(), status);
        return ecs;
    }
}
