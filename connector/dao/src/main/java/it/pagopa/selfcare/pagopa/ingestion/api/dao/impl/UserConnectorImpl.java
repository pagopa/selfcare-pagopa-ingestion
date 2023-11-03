package it.pagopa.selfcare.pagopa.ingestion.api.dao.impl;

import it.pagopa.selfcare.pagopa.ingestion.api.dao.mapper.ECMapper;
import it.pagopa.selfcare.pagopa.ingestion.api.dao.mapper.UserMapper;
import it.pagopa.selfcare.pagopa.ingestion.api.dao.model.UserEntity;
import it.pagopa.selfcare.pagopa.ingestion.api.dao.repo.UserRepository;
import it.pagopa.selfcare.pagopa.ingestion.api.dao.utils.MaskData;
import it.pagopa.selfcare.pagopa.ingestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.ingestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.EC;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.Role;
import it.pagopa.selfcare.pagopa.ingestion.model.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserConnectorImpl implements UserConnector {

    private final UserRepository repository;
    private final int pageSize;


    public UserConnectorImpl(UserRepository repository, @Value("${app.pageSize}") int pageSize) {
        this.repository = repository;
        this.pageSize = pageSize;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.dtoToEntity(user);
        UserEntity savedEntity = repository.save(entity);
        log.info("Salvato User con correlationId: {}", it.pagopa.selfcare.pagopa.ingestion.utils.MaskData.maskData(savedEntity.getCorrelationId()));
        return UserMapper.entityToDto(savedEntity);
    }

    @Override
    public List<User> saveAll(List<User> userList) {
        List<User> savedUserList = userList.stream()
                .map(this::save)
                .collect(Collectors.toList());
        log.info("Salvati {} elementi User", savedUserList.size());
        return savedUserList;
    }

    @Override
    public List<User> findAllByStatus(int page, int pageSize, String status) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<User> users = repository.findAllByStatus(status, pageRequest)
                .stream()
                .map(UserMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi User con workStatus '{}'", users.size(), status);
        return users;

    }

    @Override
    public User findManagerByInstitutionTaxCodeAndRole(String institutionTaxCode, Role role) {
        return repository.findAllByInstitutionTaxCodeAndRole(institutionTaxCode, role.name())
                .stream()
                .findFirst()
                .map(UserMapper::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Manager for institution %s not found", institutionTaxCode)));
    }

}
