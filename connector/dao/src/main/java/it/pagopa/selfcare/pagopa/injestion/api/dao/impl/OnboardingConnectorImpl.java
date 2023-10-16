package it.pagopa.selfcare.pagopa.injestion.api.dao.impl;

import it.pagopa.selfcare.pagopa.injestion.api.dao.mapper.OnboardingMapper;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.OnboardingEntity;
import it.pagopa.selfcare.pagopa.injestion.api.dao.repo.OnboardingRepository;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.OnboardingConnector;
import it.pagopa.selfcare.pagopa.injestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.injestion.model.dto.Onboarding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OnboardingConnectorImpl implements OnboardingConnector {

    private final OnboardingRepository repository;

    public OnboardingConnectorImpl(OnboardingRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Onboarding> findAll(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<Onboarding> onboardingList = repository.findAll(pageRequest)
                .stream()
                .map(OnboardingMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi Onboarding paginati (pagina {}, dimensione {})", onboardingList.size(), page, pageSize);
        return onboardingList;
    }

    @Override
    public List<Onboarding> findAll() {
        List<Onboarding> onboardingList = repository.findAll()
                .stream()
                .map(OnboardingMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi Onboarding", onboardingList.size());
        return onboardingList;
    }

    @Override
    public Onboarding findById(String id) {
        OnboardingEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Onboarding NOT FOUND " + id));
        log.info("Trovato Onboarding con id: {}", entity.getId());
        return OnboardingMapper.entityToDto(entity);
    }

    @Override
    public Onboarding save(Onboarding onboarding) {
        OnboardingEntity entity = OnboardingMapper.dtoToEntity(onboarding);
        OnboardingEntity savedEntity = repository.save(entity);
        log.info("Salvato Onboarding con id: {}", savedEntity.getId());
        return OnboardingMapper.entityToDto(savedEntity);
    }

    @Override
    public List<Onboarding> saveAll(List<Onboarding> onboardingList) {
        List<Onboarding> savedOnboardingList = onboardingList.stream()
                .map(this::save)
                .collect(Collectors.toList());
        log.info("Salvati {} elementi Onboarding", savedOnboardingList.size());
        return savedOnboardingList;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
        log.info("Eliminato Onboarding con id: {}", id);
    }

    @Override
    public void delete(Onboarding onboarding) {
        OnboardingEntity entity = OnboardingMapper.dtoToEntity(onboarding);
        repository.delete(entity);
        log.info("Eliminato Onboarding con id: {}", entity.getId());
    }

    @Override
    public List<Onboarding> findAllByStatus(int page, int pageSize, String status) {
        Query query = new Query(Criteria.where("status").is(status));
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<Onboarding> onboardingList = repository.find(query, pageRequest, OnboardingEntity.class)
                .stream()
                .map(OnboardingMapper::entityToDto)
                .collect(Collectors.toList());

        log.info("Trovati {} elementi Onboarding paginati (pagina {}, dimensione {}) con status: {}", onboardingList.size(), page, pageSize, status);
        return onboardingList;
    }

    @Override
    public List<Onboarding> findAllByStatus(String status) {
        Query query = new Query(Criteria.where("status").is(status));
        List<Onboarding> onboardingList = repository.find(query, OnboardingEntity.class)
                .stream()
                .map(OnboardingMapper::entityToDto)
                .collect(Collectors.toList());

        log.info("Trovati {} elementi Onboarding con status: {}", onboardingList.size(), status);
        return onboardingList;
    }

}
