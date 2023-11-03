package it.pagopa.selfcare.pagopa.ingestion.api.dao.impl;

import it.pagopa.selfcare.pagopa.ingestion.api.dao.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.ingestion.api.dao.model.ECEntity;
import it.pagopa.selfcare.pagopa.ingestion.api.dao.repo.ECRepository;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.ECConnector;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.EC;
import it.pagopa.selfcare.pagopa.ingestion.utils.MaskData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ECConnectorImpl implements ECConnector {

    private final ECRepository repository;

    public ECConnectorImpl(ECRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<EC> findAllByStatus(int page, int pageSize, String status) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<EC> ecs = repository.findAllByStatus(status, pageRequest)
                .stream()
                .map(ECMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi EC con workStatus '{}'", ecs.size(), status);
        return ecs;
    }

    @Override
    public EC save(EC ec) {
        final ECEntity entity = ECMapper.dtoToEntity(ec);
        ECEntity savedEntity = repository.save(entity);
        log.info("Salvato EC con taxCode: {}", MaskData.maskData(savedEntity.getTaxCode()));
        return ECMapper.entityToDto(savedEntity);
    }
}
