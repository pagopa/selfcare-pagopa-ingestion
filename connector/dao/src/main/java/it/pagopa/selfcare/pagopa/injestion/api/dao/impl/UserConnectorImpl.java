package it.pagopa.selfcare.pagopa.injestion.api.dao.impl;

import it.pagopa.selfcare.pagopa.injestion.api.dao.mapper.UserMapper;
import it.pagopa.selfcare.pagopa.injestion.api.dao.model.UserEntity;
import it.pagopa.selfcare.pagopa.injestion.api.dao.repo.UserRepository;
import it.pagopa.selfcare.pagopa.injestion.api.dao.utils.MaskData;
import it.pagopa.selfcare.pagopa.injestion.api.mongo.UserConnector;
import it.pagopa.selfcare.pagopa.injestion.exception.ResourceNotFoundException;
import it.pagopa.selfcare.pagopa.injestion.model.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserConnectorImpl implements UserConnector {

    private final UserRepository repository;

    public UserConnectorImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.dtoToEntity(user);
        UserEntity savedEntity = repository.save(entity);
        log.info("Salvato User con correlationId: {}", it.pagopa.selfcare.pagopa.injestion.utils.MaskData.maskData(savedEntity.getCorrelationId()));
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
    public List<User> findAllByInstitutionTaxCode(String institutionTaxCode) {
        List<User> userList = repository.findAllByInstitutionTaxCode(institutionTaxCode)
                .stream()
                .map(UserMapper::entityToDto)
                .collect(Collectors.toList());
        log.info("Trovati {} elementi User con codice fiscale '{}'", userList.size(), MaskData.maskData(institutionTaxCode));
        return userList;
    }

}
